package com.ranjan.somiq.profile.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.profile.data.model.ProfileResponse

@Stable
data class ProfileUiState(
    val profile: ProfileResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val refreshing: Boolean = false
) {
    val hasError: Boolean
        get() = error != null && profile == null
}

sealed interface ProfileAction {
    object LoadProfile : ProfileAction
    object RefreshProfile : ProfileAction
    object ClearError : ProfileAction
    object Retry : ProfileAction
}

sealed interface ProfileEvent {
    data class NavigateToEditProfile(val userId: String) : ProfileEvent
    data class NavigateToSettings(val userId: String) : ProfileEvent
    data class NavigateToFollowers(val userId: String) : ProfileEvent
    data class NavigateToFollowing(val userId: String) : ProfileEvent
}
