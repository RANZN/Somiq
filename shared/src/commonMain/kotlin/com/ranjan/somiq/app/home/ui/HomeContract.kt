package com.ranjan.somiq.app.home.ui

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object HomeContract {

    data class UiState(
        val selectedTab: HomeTab = HomeTab.Updates,
        val searchQuery: String = "",
        val currentUserName: String = "Profile",
        val isLoadingProfile: Boolean = false,
        val scrollToTopKey: Int = 0
    ) : BaseUiState

    sealed interface Intent : BaseUiIntent {
        data class SelectTab(val tab: HomeTab) : Intent
        data class SearchQueryChange(val query: String) : Intent
        data object Logout : Intent
        data object LoadCurrentUserProfile : Intent

        // Navigation Intents
        data class NavigateToUser(val userId: String) : Intent
        data class NavigateToPost(val postId: String) : Intent
        data class NavigateToComments(val postId: String) : Intent
        data class NavigateToStory(val storyId: String) : Intent
        data class ShowShareDialog(val postId: String) : Intent
        data class ShowMoreOptions(val postId: String) : Intent
        data class NavigateToEditProfile(val userId: String) : Intent
        data class NavigateToSettings(val userId: String) : Intent
        data class NavigateToFollowers(val userId: String) : Intent
        data class NavigateToFollowing(val userId: String) : Intent
        data class NavigateToConversation(val userId: String) : Intent
        data object NavigateToNotifications : Intent
        data object NavigateToCreatePost : Intent
        data object NavigateToCreateStory : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object Logout : Effect

        // Navigation Effects
        data class NavigateToUser(val userId: String) : Effect
        data class NavigateToPost(val postId: String) : Effect
        data class NavigateToComments(val postId: String) : Effect
        data class NavigateToStory(val storyId: String) : Effect
        data class ShowShareDialog(val postId: String) : Effect
        data class ShowMoreOptions(val postId: String) : Effect
        data class NavigateToEditProfile(val userId: String) : Effect
        data class NavigateToSettings(val userId: String) : Effect
        data class NavigateToFollowers(val userId: String) : Effect
        data class NavigateToFollowing(val userId: String) : Effect
        data class NavigateToConversation(val userId: String) : Effect
        data object NavigateToNotifications : Effect
        data object NavigateToCreatePost : Effect
        data object NavigateToCreateStory : Effect
    }
}
