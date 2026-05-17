package com.ranjan.somiq.feed.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_load_feed
import com.ranjan.somiq.core.resources.error_failed_to_load_more
import com.ranjan.somiq.core.resources.error_failed_to_refresh_feed
import com.ranjan.somiq.core.resources.error_failed_to_update_bookmark
import com.ranjan.somiq.core.resources.error_failed_to_update_like
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story

object FeedContract {
    sealed class ScreenError : BaseScreenError {
        data object LoadFeedFailed : ScreenError()
        data object RefreshFeedFailed : ScreenError()
        data object LoadMoreFailed : ScreenError()
        data object ToggleLikeFailed : ScreenError()
        data object ToggleBookmarkFailed : ScreenError()

        override fun toUiText(): UiText = when (this) {
            LoadFeedFailed -> UiText.Resource(Res.string.error_failed_to_load_feed)
            RefreshFeedFailed -> UiText.Resource(Res.string.error_failed_to_refresh_feed)
            LoadMoreFailed -> UiText.Resource(Res.string.error_failed_to_load_more)
            ToggleLikeFailed -> UiText.Resource(Res.string.error_failed_to_update_like)
            ToggleBookmarkFailed -> UiText.Resource(Res.string.error_failed_to_update_bookmark)
        }
    }

    @Stable
    data class UiState(
        val posts: List<Post> = emptyList(),
        val stories: List<Story> = emptyList(),
        val nextCursor: String? = null,
        val loading: Boolean = false,
        val loadingMore: Boolean = false,
        val error: AppError? = null,
        val refreshing: Boolean = false
    ) : BaseUiState {
        val hasMore: Boolean
            get() = nextCursor != null

        val showLoading get() = loading && posts.isEmpty()
        val showError get() = error != null && posts.isEmpty()
    }

    sealed interface Intent : BaseUiIntent {
        object LoadFeed : Intent
        object LoadMore : Intent
        object RefreshFeed : Intent
        object LoadStories : Intent

        data class ToggleLike(val postId: String) : Intent
        data class ToggleBookmark(val postId: String) : Intent
        data class OnPostClick(val postId: String) : Intent
        data class OnCommentClick(val postId: String) : Intent
        data class OnShareClick(val postId: String) : Intent
        data class OnMoreClick(val postId: String) : Intent

        data class OnUserClick(val userId: String) : Intent
        data class OnStoryClick(val storyId: String) : Intent

        object OnCreatePostClick : Intent
        object OnNotificationsClick : Intent
        object OnChatClick : Intent
        object OnAddStoryClick : Intent

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
