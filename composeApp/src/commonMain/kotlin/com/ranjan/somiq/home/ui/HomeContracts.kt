package com.ranjan.somiq.home.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.home.data.model.Post
import com.ranjan.somiq.home.data.model.Story

@Stable
data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val stories: List<Story> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val refreshing: Boolean = false
) {
    val isEmpty: Boolean
        get() = posts.isEmpty() && !isLoading && error == null

    val hasError: Boolean
        get() = error != null && posts.isEmpty()
}

sealed interface HomeAction {
    // Feed actions
    object LoadFeed : HomeAction
    object RefreshFeed : HomeAction
    object LoadStories : HomeAction

    // Post interaction actions
    data class ToggleLike(val postId: String) : HomeAction
    data class ToggleBookmark(val postId: String) : HomeAction
    data class OnPostClick(val postId: String) : HomeAction
    data class OnCommentClick(val postId: String) : HomeAction
    data class OnShareClick(val postId: String) : HomeAction
    data class OnMoreClick(val postId: String) : HomeAction

    // User actions
    data class OnUserClick(val userId: String) : HomeAction
    data class OnStoryClick(val storyId: String) : HomeAction

    // Error handling
    object ClearError : HomeAction
    object Retry : HomeAction
}

sealed interface HomeEvent {
    data class NavigateToPost(val postId: String) : HomeEvent
    data class NavigateToUser(val userId: String) : HomeEvent
    data class NavigateToComments(val postId: String) : HomeEvent
    data class ShowShareDialog(val postId: String) : HomeEvent
    data class ShowMoreOptions(val postId: String) : HomeEvent
    data class NavigateToStory(val storyId: String) : HomeEvent
}

