package com.ranjan.somiq.reels.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.reels.domain.usecase.GetReelsUseCase
import com.ranjan.somiq.reels.ui.ReelsContract.Effect
import com.ranjan.somiq.reels.ui.ReelsContract.Intent
import com.ranjan.somiq.reels.ui.ReelsContract.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReelsViewModel(
    private val getReelsUseCase: GetReelsUseCase
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
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
                is Intent.ClearError -> _uiState.update {it.copy(error = null) }
                is Intent.Retry -> {
                    _uiState.update {it.copy(error = null) }
                    loadReels()
                }
            }
        }
    }
    private suspend fun loadReels() {
        _uiState.update {it.copy(isLoading = true, error = null) }
        getReelsUseCase().getOrElse { error ->
            _uiState.update {it.copy(
                    isLoading = false,
                    error = error.toAppError(ReelsContract.ScreenError.LoadReelsFailed)
                )
            }
            return
        }.let { reels ->
            _uiState.update {it.copy(
                    reels = reels,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
    private suspend fun refreshReels() {
        _uiState.update {it.copy(refreshing = true, error = null) }
        getReelsUseCase().getOrElse { error ->
            val appError = error.toAppError(ReelsContract.ScreenError.RefreshReelsFailed)
            _uiState.update {it.copy(refreshing = false, error = appError) }
            showSnackbar(appError)
            return
        }.let { reels ->
            _uiState.update {it.copy(
                    reels = reels,
                    refreshing = false,
                    error = null
                )
            }
        }
    }
}
