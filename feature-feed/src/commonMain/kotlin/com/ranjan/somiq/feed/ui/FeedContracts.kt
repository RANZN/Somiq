package com.ranjan.somiq.feed.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story

object FeedContract {
    @Stable
    data class UiState(
        val posts: List<Post> = emptyList(),
        val stories: List<Story> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false
    ): BaseUiState {
        val isEmpty: Boolean
            get() = posts.isEmpty() && !isLoading && error == null

        val hasError: Boolean
            get() = error != null && posts.isEmpty()
    }

    sealed interface Action : BaseUiAction {
        // Feed actions
        object LoadFeed : Action
        object RefreshFeed : Action
        object LoadStories : Action

        // Post interaction actions
        data class ToggleLike(val postId: String) : Action
        data class ToggleBookmark(val postId: String) : Action
        data class OnPostClick(val postId: String) : Action
        data class OnCommentClick(val postId: String) : Action
        data class OnShareClick(val postId: String) : Action
        data class OnMoreClick(val postId: String) : Action

        // User actions
        data class OnUserClick(val userId: String) : Action
        data class OnStoryClick(val storyId: String) : Action

        // Error handling
        object ClearError : Action
        object Retry : Action
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToPost(val postId: String) : Effect
        data class NavigateToUser(val userId: String) : Effect
        data class NavigateToComments(val postId: String) : Effect
        data class ShowShareDialog(val postId: String) : Effect
        data class ShowMoreOptions(val postId: String) : Effect
        data class NavigateToStory(val storyId: String) : Effect
    }
}
