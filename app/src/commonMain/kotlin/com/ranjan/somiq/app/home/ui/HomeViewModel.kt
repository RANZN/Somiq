package com.ranjan.somiq.app.home.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.usecase.LogoutUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.presentation.navigation.Home
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : BaseViewModel<HomeContract.UiState, HomeContract.Action, HomeContract.Effect>() {

    override val initialState: HomeContract.UiState
        get() = HomeContract.UiState()

    init {
        loadCurrentUserProfile()
    }

    override fun onAction(action: HomeContract.Action) {
        viewModelScope.launch {
            when (action) {
                is HomeContract.Action.SelectTab -> {
                    setState { copy(selectedTab = action.tab) }
                }
                is HomeContract.Action.SearchQueryChange -> {
                    setState { copy(searchQuery = action.query) }
                }
                HomeContract.Action.Logout -> {
                    logoutUseCase()
                    emitEffect(HomeContract.Effect.NavigateToLogin)
                }
                HomeContract.Action.LoadCurrentUserProfile -> {
                    loadCurrentUserProfile()
                }
            }
        }
    }

    private fun loadCurrentUserProfile() {
        viewModelScope.launch {
            setState { copy(isLoadingProfile = true) }
            getProfileUseCase(userId = null)
                .onSuccess { response ->
                    setState {
                        copy(
                            currentUserName = response.user.name,
                            isLoadingProfile = false
                        )
                    }
                }
                .onFailure {
                    setState { copy(isLoadingProfile = false) }
                }
        }
    }
}
