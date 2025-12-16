package com.ranjan.somiq.profile.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import com.ranjan.somiq.profile.ui.ProfileContract.Action
import com.ranjan.somiq.profile.ui.ProfileContract.Effect
import com.ranjan.somiq.profile.ui.ProfileContract.UiState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : BaseViewModel<UiState, Action, Effect>(UiState()) {
    
    private var userId: String? = null
    
    fun setUserId(userId: String?) {
        this.userId = userId
    }

    init {
        handleAction(Action.LoadProfile)
    }

    override fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.LoadProfile -> loadProfile()
                is Action.RefreshProfile -> refreshProfile()
                is Action.ClearError -> setState { copy(error = null) }
                is Action.Retry -> {
                    setState { copy(error = null) }
                    loadProfile()
                }
            }
        }
    }

    private suspend fun loadProfile() {
        setState { copy(isLoading = true, error = null) }
        getProfileUseCase(userId).getOrElse { error ->
            setState {
                copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load profile"
                )
            }
            return
        }.let { profile ->
            setState {
                copy(
                    profile = profile,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun refreshProfile() {
        setState { copy(refreshing = true, error = null) }
        getProfileUseCase(userId).getOrElse { error ->
            setState {
                copy(
                    refreshing = false,
                    error = error.message ?: "Failed to refresh profile"
                )
            }
            return
        }.let { profile ->
            setState {
                copy(
                    profile = profile,
                    refreshing = false,
                    error = null
                )
            }
        }
    }
}
