package com.ranjan.somiq.reels.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_load_reels
import com.ranjan.somiq.core.resources.error_failed_to_refresh_reels
import com.ranjan.somiq.reels.data.model.Reel

object ReelsContract {
    sealed class ScreenError : BaseScreenError {
        data object LoadReelsFailed : ScreenError()
        data object RefreshReelsFailed : ScreenError()
        override fun toUiText(): UiText = when (this) {
            LoadReelsFailed -> UiText.Resource(Res.string.error_failed_to_load_reels)
            RefreshReelsFailed -> UiText.Resource(Res.string.error_failed_to_refresh_reels)
        }
    }
    @Stable
    data class UiState(
        val reels: List<Reel> = emptyList(),
        val isLoading: Boolean = false,
        val error: AppError? = null,
        val refreshing: Boolean = false
    ) {
        val hasError: Boolean
            get() = error != null && reels.isEmpty()
        val isEmpty: Boolean
            get() = reels.isEmpty() && !isLoading && error == null
    }
    sealed interface Intent : BaseUiIntent {
        object LoadReels : Intent
        object RefreshReels : Intent
        data class OnReelClick(val reelId: String) : Intent
        data class OnLikeClick(val reelId: String) : Intent
        data class OnCommentClick(val reelId: String) : Intent
        data class OnShareClick(val reelId: String) : Intent
        object ClearError : Intent
        object Retry : Intent
    }
    sealed interface Effect : BaseUiEffect {
        data class NavigateToReel(val reelId: String) : Effect
        data class NavigateToComments(val reelId: String) : Effect
        data class ShowShareDialog(val reelId: String) : Effect
    }
}
