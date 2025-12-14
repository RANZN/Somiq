package com.ranjan.somiq.feed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.feed.domain.usecase.GetFeedUseCase
import com.ranjan.somiq.feed.domain.usecase.GetStoriesUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleLikeUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getFeedUseCase: GetFeedUseCase,
    private val getStoriesUseCase: GetStoriesUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _events = Channel<FeedEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {
        handleAction(FeedAction.LoadFeed)
        handleAction(FeedAction.LoadStories)
    }

    fun handleAction(action: FeedAction) {
        viewModelScope.launch {
            when (action) {
                is FeedAction.LoadFeed -> loadFeed()
                is FeedAction.RefreshFeed -> refreshFeed()
                is FeedAction.LoadStories -> loadStories()
                is FeedAction.ToggleLike -> toggleLike(action.postId)
                is FeedAction.ToggleBookmark -> toggleBookmark(action.postId)
                is FeedAction.OnPostClick -> _events.send(FeedEvent.NavigateToPost(action.postId))
                is FeedAction.OnCommentClick -> _events.send(FeedEvent.NavigateToComments(action.postId))
                is FeedAction.OnShareClick -> _events.send(FeedEvent.ShowShareDialog(action.postId))
                is FeedAction.OnMoreClick -> _events.send(FeedEvent.ShowMoreOptions(action.postId))
                is FeedAction.OnUserClick -> _events.send(FeedEvent.NavigateToUser(action.userId))
                is FeedAction.OnStoryClick -> _events.send(FeedEvent.NavigateToStory(action.storyId))
                is FeedAction.ClearError -> _uiState.update { it.copy(error = null) }
                is FeedAction.Retry -> {
                    _uiState.update { it.copy(error = null) }
                    loadFeed()
                }
            }
        }
    }

    private suspend fun loadFeed() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        getFeedUseCase().getOrElse { error ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load feed"
                )
            }
            return
        }.let { posts ->
            _uiState.update {
                it.copy(
                    posts = posts,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun refreshFeed() {
        _uiState.update { it.copy(refreshing = true, error = null) }
        getFeedUseCase().getOrElse { error ->
            _uiState.update {
                it.copy(
                    refreshing = false,
                    error = error.message ?: "Failed to refresh feed"
                )
            }
            return
        }.let { posts ->
            _uiState.update {
                it.copy(
                    posts = posts,
                    refreshing = false,
                    error = null
                )
            }
        }
    }

    private suspend fun loadStories() {
        getStoriesUseCase().getOrElse {
            // Stories are optional, don't show error
            return
        }.let { stories ->
            _uiState.update { it.copy(stories = stories) }
        }
    }

    private suspend fun toggleLike(postId: String) {
        val currentState = _uiState.value
        val post = currentState.posts.find { it.id == postId } ?: return
        val wasLiked = post.isLiked

        // Optimistic update
        _uiState.update { state ->
            state.copy(
                posts = state.posts.map {
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
                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.map {
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
                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.map {
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
        val currentState = _uiState.value
        val post = currentState.posts.find { it.id == postId } ?: return
        val wasBookmarked = post.isBookmarked

        // Optimistic update
        _uiState.update { state ->
            state.copy(
                posts = state.posts.map {
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
                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.map {
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
                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.map {
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
