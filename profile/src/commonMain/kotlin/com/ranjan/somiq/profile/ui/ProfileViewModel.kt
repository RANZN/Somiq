package com.ranjan.somiq.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    
    private var userId: String? = null
    
    fun setUserId(userId: String?) {
        this.userId = userId
    }

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
        getProfileUseCase(userId).getOrElse { error ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load profile"
                )
            }
            return
        }.let { profile ->
            _uiState.update {
                it.copy(
                    profile = profile,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun refreshProfile() {
        _uiState.update { it.copy(refreshing = true, error = null) }
        getProfileUseCase(userId).getOrElse { error ->
            _uiState.update {
                it.copy(
                    refreshing = false,
                    error = error.message ?: "Failed to refresh profile"
                )
            }
            return
        }.let { profile ->
            _uiState.update {
                it.copy(
                    profile = profile,
                    refreshing = false,
                    error = null
                )
            }
        }
    }
}
