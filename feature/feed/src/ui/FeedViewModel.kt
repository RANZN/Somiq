package com.ranjan.somiq.feed.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.domain.PostUploadService
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.usecase.GetFeedPageUseCase
import com.ranjan.somiq.feed.domain.usecase.GetStoriesUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleBookmarkUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleLikeUseCase
import com.ranjan.somiq.feed.ui.FeedContract.Effect
import com.ranjan.somiq.feed.ui.FeedContract.Intent
import com.ranjan.somiq.feed.ui.FeedContract.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getFeedPageUseCase: GetFeedPageUseCase,
    private val getStoriesUseCase: GetStoriesUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val postUploadService: PostUploadService
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    init {
        handleIntent(Intent.LoadFeed)
        handleIntent(Intent.LoadStories)
        observeUploadState()
    }
    private fun observeUploadState() {
        viewModelScope.launch {
            postUploadService.uploadState.collect { uploadState ->
                _uiState.update {it.copy(uploadState = uploadState) }
                if (uploadState is com.ranjan.somiq.core.domain.UploadState.Success) {
                    refreshFeed()
                }
            }
        }
    }
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.LoadFeed -> loadFeed()
                is Intent.LoadMore -> loadMore()
                is Intent.RefreshFeed -> refreshFeed()
                is Intent.LoadStories -> loadStories()
                is Intent.ToggleLike -> toggleLike(intent.postId)
                is Intent.ToggleBookmark -> toggleBookmark(intent.postId)
                is Intent.OnPostClick -> emitEffect(Effect.NavigateToPost(intent.postId))
                is Intent.OnCommentClick -> emitEffect(Effect.NavigateToComments(intent.postId))
                is Intent.OnShareClick -> emitEffect(Effect.ShowShareDialog(intent.postId))
                is Intent.OnMoreClick -> emitEffect(Effect.ShowMoreOptions(intent.postId))
                is Intent.OnUserClick -> emitEffect(Effect.NavigateToUser(intent.userId))
                is Intent.OnStoryClick -> emitEffect(Effect.NavigateToStory(intent.storyId))
                is Intent.OnCreatePostClick -> emitEffect(Effect.NavigateToCreatePost)
                is Intent.OnNotificationsClick -> emitEffect(Effect.NavigateToNotifications)
                is Intent.OnChatClick -> emitEffect(Effect.NavigateToChat)
                is Intent.OnAddStoryClick -> emitEffect(Effect.NavigateToCreateStory)
                is Intent.ClearError -> _uiState.update {it.copy(error = null) }
                is Intent.DismissUploadProgress -> postUploadService.resetToIdle()
                is Intent.Retry -> {
                    _uiState.update {it.copy(error = null) }
                    loadFeed()
                    loadStories()
                }
            }
        }
    }
    private suspend fun loadFeed() {
        _uiState.update {it.copy(loading = true, error = null) }
        getFeedPageUseCase().getOrElse { error ->
            _uiState.update {it.copy(
                    loading = false,
                    error = error.toAppError(FeedContract.ScreenError.LoadFeedFailed)
                )
            }
            return
        }.let { result ->
            _uiState.update {it.copy(
                    posts = result.data,
                    nextCursor = result.nextCursor,
                    loading = false,
                    error = null
                )
            }
        }
    }
    private suspend fun loadMore() {
        val cursor = uiState.value.nextCursor ?: return
        if (uiState.value.loadingMore) return
        _uiState.update {it.copy(loadingMore = true) }
        getFeedPageUseCase(after = cursor).getOrElse { error ->
            _uiState.update {it.copy(loadingMore = false) }
            showSnackbar(error.toAppError(FeedContract.ScreenError.LoadMoreFailed))
            return
        }.let { result ->
            _uiState.update {it.copy(
                    posts = it.posts + result.data,
                    nextCursor = result.nextCursor,
                    loadingMore = false
                )
            }
        }
    }
    private suspend fun refreshFeed() {
        _uiState.update {it.copy(refreshing = true, error = null) }
        getFeedPageUseCase(after = null).getOrElse { error ->
            val appError = error.toAppError(FeedContract.ScreenError.RefreshFeedFailed)
            _uiState.update {it.copy(refreshing = false, error = appError) }
            showSnackbar(appError)
            return
        }.let { result ->
            _uiState.update {it.copy(
                    posts = result.data,
                    nextCursor = result.nextCursor,
                    error = null
                )
            }
        }
        loadStories()
        _uiState.update {it.copy(refreshing = false) }
    }
    private suspend fun loadStories() {
        getStoriesUseCase().getOrElse {
            // Stories are optional, don't show error
            return
        }.let { stories ->
            _uiState.update {it.copy(stories = stories) }
        }
    }
    private suspend fun toggleLike(postId: String) {
        val currentState = uiState.value
        val post = currentState.posts.find { it.id == postId } ?: return
        val wasLiked = post.isLiked
        // Optimistic update
        _uiState.update {it.copy(
                posts = it.posts.map { post ->
                    if (post.id == postId) {
                        post.copy(
                            isLiked = !post.isLiked,
                            likesCount = if (wasLiked) post.likesCount - 1 else post.likesCount + 1
                        )
                    } else {
                        post
                    }
                }
            )
        }
        // Actual API call - toggle like
        val result = toggleLikeUseCase(postId)
        result.fold(
            onSuccess = { toggleResponse ->
                // Sync with server response to ensure accuracy
                _uiState.update {it.copy(
                        posts = it.posts.map { post ->
                            if (post.id == postId) {
                                post.copy(
                                    isLiked = toggleResponse.isLiked,
                                    isBookmarked = toggleResponse.isBookmarked,
                                    likesCount = toggleResponse.likesCount,
                                    bookmarksCount = toggleResponse.bookmarksCount
                                )
                            } else {
                                post
                            }
                        }
                    )
                }
            },
            onFailure = {
                _uiState.update {it.copy(
                        posts = it.posts.map { post ->
                            if (post.id == postId) {
                                post.copy(
                                    isLiked = wasLiked,
                                    likesCount = if (wasLiked) post.likesCount + 1 else post.likesCount - 1
                                )
                            } else {
                                post
                            }
                        }
                    )
                }
                showSnackbar(FeedContract.ScreenError.ToggleLikeFailed)
            }
        )
    }
    private suspend fun toggleBookmark(postId: String) {
        val currentState = uiState.value
        val post = currentState.posts.find { it.id == postId } ?: return
        val wasBookmarked = post.isBookmarked
        // Optimistic update
        _uiState.update {it.copy(
                posts = it.posts.map { post ->
                    if (post.id == postId) {
                        post.copy(isBookmarked = !post.isBookmarked)
                    } else {
                        post
                    }
                }
            )
        }
        // Actual API call - toggle bookmark
        val result = toggleBookmarkUseCase(postId)
        result.fold(
            onSuccess = { toggleResponse ->
                // Sync with server response to ensure accuracy
                _uiState.update {it.copy(
                        posts = it.posts.map { post ->
                            if (post.id == postId) {
                                post.copy(
                                    isLiked = toggleResponse.isLiked,
                                    isBookmarked = toggleResponse.isBookmarked,
                                    likesCount = toggleResponse.likesCount,
                                    bookmarksCount = toggleResponse.bookmarksCount
                                )
                            } else {
                                post
                            }
                        }
                    )
                }
            },
            onFailure = {
                _uiState.update {it.copy(
                        posts = it.posts.map { post ->
                            if (post.id == postId) {
                                post.copy(isBookmarked = wasBookmarked)
                            } else {
                                post
                            }
                        }
                    )
                }
                showSnackbar(FeedContract.ScreenError.ToggleBookmarkFailed)
            }
        )
    }
}
