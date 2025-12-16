package com.ranjan.somiq.reels.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.reels.domain.usecase.GetReelsUseCase
import com.ranjan.somiq.reels.ui.ReelsContract.Action
import com.ranjan.somiq.reels.ui.ReelsContract.Effect
import com.ranjan.somiq.reels.ui.ReelsContract.UiState
import kotlinx.coroutines.launch

class ReelsViewModel(
    private val getReelsUseCase: GetReelsUseCase
) : BaseViewModel<UiState, Action, Effect>(UiState()) {

    init {
        handleAction(Action.LoadReels)
    }

    override fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.LoadReels -> loadReels()
                is Action.RefreshReels -> refreshReels()
                is Action.OnReelClick -> emitEffect(Effect.NavigateToReel(action.reelId))
                is Action.OnLikeClick -> {
                    // TODO: Implement like functionality
                }
                is Action.OnCommentClick -> emitEffect(Effect.NavigateToComments(action.reelId))
                is Action.OnShareClick -> emitEffect(Effect.ShowShareDialog(action.reelId))
                is Action.ClearError -> setState { copy(error = null) }
                is Action.Retry -> {
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
