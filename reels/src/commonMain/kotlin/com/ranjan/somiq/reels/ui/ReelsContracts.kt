package com.ranjan.somiq.reels.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.reels.data.model.Reel

object ReelsContract {
    @Stable
    data class UiState(
        val reels: List<Reel> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false
    ) : BaseUiState {
        val hasError: Boolean
            get() = error != null && reels.isEmpty()

        val isEmpty: Boolean
            get() = reels.isEmpty() && !isLoading && error == null
    }

    sealed interface Action : BaseUiAction {
        object LoadReels : Action
        object RefreshReels : Action
        data class OnReelClick(val reelId: String) : Action
        data class OnLikeClick(val reelId: String) : Action
        data class OnCommentClick(val reelId: String) : Action
        data class OnShareClick(val reelId: String) : Action
        object ClearError : Action
        object Retry : Action
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToReel(val reelId: String) : Effect
        data class NavigateToComments(val reelId: String) : Effect
        data class ShowShareDialog(val reelId: String) : Effect
    }
}
