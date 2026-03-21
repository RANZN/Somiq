package com.ranjan.somiq.feed.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.usecase.GetFeedPageUseCase
import com.ranjan.somiq.feed.domain.usecase.GetStoriesUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleBookmarkUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleLikeUseCase
import com.ranjan.somiq.feed.ui.FeedContract.Effect
import com.ranjan.somiq.feed.ui.FeedContract.Intent
import com.ranjan.somiq.feed.ui.FeedContract.UiState
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getFeedPageUseCase: GetFeedPageUseCase,
    private val getStoriesUseCase: GetStoriesUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<UiState, Intent, Effect>() {

    override val initialState: UiState
        get() = UiState()

    init {
        handleIntent(Intent.LoadFeed)
        handleIntent(Intent.LoadStories)
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
                is Intent.ClearError -> setState { copy(error = null) }
                is Intent.Retry -> {
                    setState { copy(error = null) }
                    loadFeed()
                    loadStories()
                }
            }
        }
    }

    private suspend fun loadFeed() {
        setState { copy(isLoading = true, error = null) }
        getFeedPageUseCase(after = null).getOrElse { error ->
            setState {
                copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load feed"
                )
            }
            return
        }.let { result ->
            setState {
                copy(
                    posts = result.data,
                    nextCursor = result.nextCursor,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun loadMore() {
        val cursor = state.value.nextCursor ?: return
        if (state.value.loadingMore) return
        setState { copy(loadingMore = true) }
        getFeedPageUseCase(after = cursor).getOrElse { error ->
            setState { copy(loadingMore = false) }
            return
        }.let { result ->
            setState {
                copy(
                    posts = posts + result.data,
                    nextCursor = result.nextCursor,
                    loadingMore = false
                )
            }
        }
    }

    private suspend fun refreshFeed() {
        setState { copy(refreshing = true, error = null) }
        getFeedPageUseCase(after = null).getOrElse { error ->
            setState {
                copy(
                    refreshing = false,
                    error = error.message ?: "Failed to refresh feed"
                )
            }
            return
        }.let { result ->
            setState {
                copy(
                    posts = result.data,
                    nextCursor = result.nextCursor,
                    error = null
                )
            }
        }
        loadStories()
        setState { copy(refreshing = false) }
    }

    private suspend fun loadStories() {
        getStoriesUseCase().getOrElse {
            // Stories are optional, don't show error
            return
        }.let { stories ->
            setState { copy(stories = stories) }
        }
    }

    private suspend fun toggleLike(postId: String) {
        val currentState = state.value
        val post = currentState.posts.find { it.id == postId } ?: return
        val wasLiked = post.isLiked

        // Optimistic update
        setState {
            copy(
                posts = posts.map {
                    if (it.id == postId) {
                        it.copy(
                            isLiked = !it.isLiked,
                            likesCount = if (wasLiked) it.likesCount - 1 else it.likesCount + 1
                        )
                    } else {
                        it
                    }
                }
            )
        }

        // Actual API call - toggle like
        val result = toggleLikeUseCase(postId)

        result.fold(
            onSuccess = { toggleResponse ->
                // Sync with server response to ensure accuracy
                setState {
                    copy(
                        posts = posts.map {
                            if (it.id == postId) {
                                it.copy(
                                    isLiked = toggleResponse.isLiked,
                                    isBookmarked = toggleResponse.isBookmarked,
                                    likesCount = toggleResponse.likesCount,
                                    bookmarksCount = toggleResponse.bookmarksCount
                                )
                            } else {
                                it
                            }
                        }
                    )
                }
            },
            onFailure = {
                // Revert on failure
                setState {
                    copy(
                        posts = posts.map {
                            if (it.id == postId) {
                                it.copy(
                                    isLiked = wasLiked,
                                    likesCount = if (wasLiked) it.likesCount + 1 else it.likesCount - 1
                                )
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        )
    }

    private suspend fun toggleBookmark(postId: String) {
        val currentState = state.value
        val post = currentState.posts.find { it.id == postId } ?: return
        val wasBookmarked = post.isBookmarked

        // Optimistic update
        setState {
            copy(
                posts = posts.map {
                    if (it.id == postId) {
                        it.copy(isBookmarked = !it.isBookmarked)
                    } else {
                        it
                    }
                }
            )
        }

        // Actual API call - toggle bookmark
        val result = toggleBookmarkUseCase(postId)

        result.fold(
            onSuccess = { toggleResponse ->
                // Sync with server response to ensure accuracy
                setState {
                    copy(
                        posts = posts.map {
                            if (it.id == postId) {
                                it.copy(
                                    isLiked = toggleResponse.isLiked,
                                    isBookmarked = toggleResponse.isBookmarked,
                                    likesCount = toggleResponse.likesCount,
                                    bookmarksCount = toggleResponse.bookmarksCount
                                )
                            } else {
                                it
                            }
                        }
                    )
                }
            },
            onFailure = {
                // Revert on failure
                setState {
                    copy(
                        posts = posts.map {
                            if (it.id == postId) {
                                it.copy(isBookmarked = wasBookmarked)
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        )
    }
}
