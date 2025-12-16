package com.ranjan.somiq.profile.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.profile.data.model.ProfileResponse

object ProfileContract {
    @Stable
    data class UiState(
        val profile: ProfileResponse? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false
    ) : BaseUiState {
        val hasError: Boolean
            get() = error != null && profile == null
    }

    sealed interface Action : BaseUiAction {
        object LoadProfile : Action
        object RefreshProfile : Action
        object ClearError : Action
        object Retry : Action
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToEditProfile(val userId: String) : Effect
        data class NavigateToSettings(val userId: String) : Effect
        data class NavigateToFollowers(val userId: String) : Effect
        data class NavigateToFollowing(val userId: String) : Effect
    }
}
