package com.ranjan.somiq.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProfileEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {
        handleAction(ProfileAction.LoadProfile)
    }

    fun handleAction(action: ProfileAction) {
        viewModelScope.launch {
            when (action) {
                is ProfileAction.LoadProfile -> loadProfile()
                is ProfileAction.RefreshProfile -> refreshProfile()
                is ProfileAction.ClearError -> _uiState.update { it.copy(error = null) }
                is ProfileAction.Retry -> {
                    _uiState.update { it.copy(error = null) }
                    loadProfile()
                }
            }
        }
    }

    private suspend fun loadProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        // TODO: Implement profile loading
        _uiState.update { it.copy(isLoading = false) }
    }

    private suspend fun refreshProfile() {
        _uiState.update { it.copy(refreshing = true, error = null) }
        // TODO: Implement profile refresh
        _uiState.update { it.copy(refreshing = false) }
    }
}

