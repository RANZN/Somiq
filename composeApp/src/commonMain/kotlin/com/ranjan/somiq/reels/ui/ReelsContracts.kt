package com.ranjan.somiq.reels.ui

import androidx.compose.runtime.Stable

@Stable
data class ReelsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val refreshing: Boolean = false
) {
    val hasError: Boolean
        get() = error != null
}

sealed interface ReelsAction {
    object LoadReels : ReelsAction
    object RefreshReels : ReelsAction
    data class OnReelClick(val reelId: String) : ReelsAction
    data class OnLikeClick(val reelId: String) : ReelsAction
    data class OnCommentClick(val reelId: String) : ReelsAction
    data class OnShareClick(val reelId: String) : ReelsAction
    object ClearError : ReelsAction
    object Retry : ReelsAction
}

sealed interface ReelsEvent {
    data class NavigateToReel(val reelId: String) : ReelsEvent
    data class NavigateToComments(val reelId: String) : ReelsEvent
    data class ShowShareDialog(val reelId: String) : ReelsEvent
}

