package com.ranjan.somiq.app.home.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.presentation.navigation.Home

object HomeContract {
    @Stable
    data class UiState(
        val selectedTab: Home = Home.Feed,
        val searchQuery: String = "",
        val currentUserName: String = "Profile",
        val isLoadingProfile: Boolean = false
    ) : BaseUiState

    sealed interface Action : BaseUiAction {
        data class SelectTab(val tab: Home) : Action
        data class SearchQueryChange(val query: String) : Action
        object Logout : Action
        object LoadCurrentUserProfile : Action
    }

    sealed interface Effect : BaseUiEffect {
        object NavigateToLogin : Effect
    }
}
