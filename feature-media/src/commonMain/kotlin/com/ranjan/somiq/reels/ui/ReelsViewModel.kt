package com.ranjan.somiq.reels.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.reels.domain.usecase.GetReelsUseCase
import com.ranjan.somiq.reels.ui.ReelsContract.Effect
import com.ranjan.somiq.reels.ui.ReelsContract.Intent
import com.ranjan.somiq.reels.ui.ReelsContract.UiState
import kotlinx.coroutines.launch

class ReelsViewModel(
    private val getReelsUseCase: GetReelsUseCase
) : BaseViewModel<UiState, Intent, Effect>(UiState()) {

    init {
        handleIntent(Intent.LoadReels)
    }

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.LoadReels -> loadReels()
                is Intent.RefreshReels -> refreshReels()
                is Intent.OnReelClick -> emitEffect(Effect.NavigateToReel(intent.reelId))
                is Intent.OnLikeClick -> {
                    // TODO: Implement like functionality
                }
                is Intent.OnCommentClick -> emitEffect(Effect.NavigateToComments(intent.reelId))
                is Intent.OnShareClick -> emitEffect(Effect.ShowShareDialog(intent.reelId))
                is Intent.ClearError -> setState { copy(error = null) }
                is Intent.Retry -> {
                    setState { copy(error = null) }
                    loadReels()
                }
            }
        }
    }

    private suspend fun loadReels() {
        setState { copy(isLoading = true, error = null) }
        getReelsUseCase().getOrElse { error ->
            setState {
                copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load reels"
                )
            }
            return
        }.let { reels ->
            setState {
                copy(
                    reels = reels,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun refreshReels() {
        setState { copy(refreshing = true, error = null) }
        getReelsUseCase().getOrElse { error ->
            setState {
                copy(
                    refreshing = false,
                    error = error.message ?: "Failed to refresh reels"
                )
            }
            return
        }.let { reels ->
            setState {
                copy(
                    reels = reels,
                    refreshing = false,
                    error = null
                )
            }
        }
    }
}
