package com.ranjan.somiq.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.home.domain.usecase.GetFeedUseCase
import com.ranjan.somiq.home.domain.usecase.GetStoriesUseCase
import com.ranjan.somiq.home.domain.usecase.ToggleLikeUseCase
import com.ranjan.somiq.home.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFeedUseCase: GetFeedUseCase,
    private val getStoriesUseCase: GetStoriesUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {
        handleAction(HomeAction.LoadFeed)
        handleAction(HomeAction.LoadStories)
    }

    fun handleAction(action: HomeAction) {
        viewModelScope.launch {
            when (action) {
                is HomeAction.LoadFeed -> loadFeed()
                is HomeAction.RefreshFeed -> refreshFeed()
                is HomeAction.LoadStories -> loadStories()
                is HomeAction.ToggleLike -> toggleLike(action.postId)
                is HomeAction.ToggleBookmark -> toggleBookmark(action.postId)
                is HomeAction.OnPostClick -> _events.send(HomeEvent.NavigateToPost(action.postId))
                is HomeAction.OnCommentClick -> _events.send(HomeEvent.NavigateToComments(action.postId))
                is HomeAction.OnShareClick -> _events.send(HomeEvent.ShowShareDialog(action.postId))
                is HomeAction.OnMoreClick -> _events.send(HomeEvent.ShowMoreOptions(action.postId))
                is HomeAction.OnUserClick -> _events.send(HomeEvent.NavigateToUser(action.userId))
                is HomeAction.OnStoryClick -> _events.send(HomeEvent.NavigateToStory(action.storyId))
                is HomeAction.ClearError -> _uiState.update { it.copy(error = null) }
                is HomeAction.Retry -> {
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

        result.getOrElse {
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
    }

    private suspend fun toggleBookmark(postId: String) {
        val currentState = _uiState.value
        val post = currentState.posts.find { it.id == postId } ?: return
        val wasSaved = post.isBookmarked

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

        result.getOrElse {
            // Revert on failure
            _uiState.update { state ->
                state.copy(
                    posts = state.posts.map {
                        if (it.id == postId) {
                            it.copy(isBookmarked = wasSaved)
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }
}

