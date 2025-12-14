package com.ranjan.somiq.reels.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.reels.domain.usecase.GetReelsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReelsViewModel(
    private val getReelsUseCase: GetReelsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReelsUiState())
    val uiState: StateFlow<ReelsUiState> = _uiState.asStateFlow()

    private val _events = Channel<ReelsEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {
        handleAction(ReelsAction.LoadReels)
    }

    fun handleAction(action: ReelsAction) {
        viewModelScope.launch {
            when (action) {
                is ReelsAction.LoadReels -> loadReels()
                is ReelsAction.RefreshReels -> refreshReels()
                is ReelsAction.OnReelClick -> _events.send(ReelsEvent.NavigateToReel(action.reelId))
                is ReelsAction.OnLikeClick -> {
                    // TODO: Implement like functionality
                }
                is ReelsAction.OnCommentClick -> _events.send(ReelsEvent.NavigateToComments(action.reelId))
                is ReelsAction.OnShareClick -> _events.send(ReelsEvent.ShowShareDialog(action.reelId))
                is ReelsAction.ClearError -> _uiState.update { it.copy(error = null) }
                is ReelsAction.Retry -> {
                    _uiState.update { it.copy(error = null) }
                    loadReels()
                }
            }
        }
    }

    private suspend fun loadReels() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        getReelsUseCase().getOrElse { error ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load reels"
                )
            }
            return
        }.let { reels ->
            _uiState.update {
                it.copy(
                    reels = reels,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun refreshReels() {
        _uiState.update { it.copy(refreshing = true, error = null) }
        getReelsUseCase().getOrElse { error ->
            _uiState.update {
                it.copy(
                    refreshing = false,
                    error = error.message ?: "Failed to refresh reels"
                )
            }
            return
        }.let { reels ->
            _uiState.update {
                it.copy(
                    reels = reels,
                    refreshing = false,
                    error = null
                )
            }
        }
    }
}
