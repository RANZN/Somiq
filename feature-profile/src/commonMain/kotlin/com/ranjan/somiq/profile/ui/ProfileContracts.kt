package com.ranjan.somiq.profile.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.profile.data.model.ProfileResponse

object ProfileContract {
    @Stable
    data class UiState(
        val profile: ProfileResponse? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false,
        val showAppBar: Boolean = false,
        val appBarTitle: String? = null
    ) : BaseUiState {
        val hasError: Boolean
            get() = error != null && profile == null
    }

    sealed interface Intent : BaseUiIntent {
        object LoadProfile : Intent
        object RefreshProfile : Intent
        data class SetAppBarConfig(val show: Boolean, val title: String?) : Intent
        object OnLogoutClick : Intent
        object ClearError : Intent
        object Retry : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToEditProfile(val userId: String) : Effect
        data class NavigateToSettings(val userId: String) : Effect
        data class NavigateToFollowers(val userId: String) : Effect
        data class NavigateToFollowing(val userId: String) : Effect
        object Logout : Effect
    }
}
