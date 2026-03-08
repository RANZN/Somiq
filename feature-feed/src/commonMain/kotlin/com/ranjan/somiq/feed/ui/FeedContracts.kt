package com.ranjan.somiq.feed.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story

object FeedContract {
    @Stable
    data class UiState(
        val posts: List<Post> = emptyList(),
        val stories: List<Story> = emptyList(),
        val nextCursor: String? = null,
        val isLoading: Boolean = false,
        val loadingMore: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false
    ) : BaseUiState {
        val hasMore: Boolean
            get() = nextCursor != null
    }

    sealed interface Intent : BaseUiIntent {
        // Feed intents
        object LoadFeed : Intent
        object LoadMore : Intent
        object RefreshFeed : Intent
        object LoadStories : Intent

        // Post interaction intents
        data class ToggleLike(val postId: String) : Intent
        data class ToggleBookmark(val postId: String) : Intent
        data class OnPostClick(val postId: String) : Intent
        data class OnCommentClick(val postId: String) : Intent
        data class OnShareClick(val postId: String) : Intent
        data class OnMoreClick(val postId: String) : Intent

        // User intents
        data class OnUserClick(val userId: String) : Intent
        data class OnStoryClick(val storyId: String) : Intent

        // Top bar / app bar intents
        object OnCreatePostClick : Intent
        object OnNotificationsClick : Intent
        object OnChatClick : Intent
        object OnAddStoryClick : Intent

        // Error handling
        object ClearError : Intent
        object Retry : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToPost(val postId: String) : Effect
        data class NavigateToUser(val userId: String) : Effect
        data class NavigateToComments(val postId: String) : Effect
        data class ShowShareDialog(val postId: String) : Effect
        data class ShowMoreOptions(val postId: String) : Effect
        data class NavigateToStory(val storyId: String) : Effect
        object NavigateToCreatePost : Effect
        object NavigateToNotifications : Effect
        object NavigateToChat : Effect
        object NavigateToCreateStory : Effect
    }
}
