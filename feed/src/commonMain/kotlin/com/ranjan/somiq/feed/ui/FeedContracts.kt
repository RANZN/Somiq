package com.ranjan.somiq.feed.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story

@Stable
data class FeedUiState(
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

sealed interface FeedAction {
    // Feed actions
    object LoadFeed : FeedAction
    object RefreshFeed : FeedAction
    object LoadStories : FeedAction

    // Post interaction actions
    data class ToggleLike(val postId: String) : FeedAction
    data class ToggleBookmark(val postId: String) : FeedAction
    data class OnPostClick(val postId: String) : FeedAction
    data class OnCommentClick(val postId: String) : FeedAction
    data class OnShareClick(val postId: String) : FeedAction
    data class OnMoreClick(val postId: String) : FeedAction

    // User actions
    data class OnUserClick(val userId: String) : FeedAction
    data class OnStoryClick(val storyId: String) : FeedAction

    // Error handling
    object ClearError : FeedAction
    object Retry : FeedAction
}

sealed interface FeedEvent {
    data class NavigateToPost(val postId: String) : FeedEvent
    data class NavigateToUser(val userId: String) : FeedEvent
    data class NavigateToComments(val postId: String) : FeedEvent
    data class ShowShareDialog(val postId: String) : FeedEvent
    data class ShowMoreOptions(val postId: String) : FeedEvent
    data class NavigateToStory(val storyId: String) : FeedEvent
}
