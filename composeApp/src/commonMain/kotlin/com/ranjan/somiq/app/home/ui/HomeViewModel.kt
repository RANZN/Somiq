package com.ranjan.somiq.app.home.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.usecase.LogoutUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : BaseViewModel<HomeContract.UiState, HomeContract.Intent, HomeContract.Effect>() {

    override val initialState: HomeContract.UiState
        get() = HomeContract.UiState()

    init {
        loadCurrentUserProfile()
    }

    override fun onIntent(intent: HomeContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is HomeContract.Intent.SelectTab -> {
                    val currentTab = state.value.selectedTab
                    setState {
                        copy(
                            selectedTab = intent.tab,
                            scrollToTopKey = if (intent.tab == currentTab) scrollToTopKey + 1 else scrollToTopKey
                        )
                    }
                }
                is HomeContract.Intent.SearchQueryChange -> {
                    setState { copy(searchQuery = intent.query) }
                }
                HomeContract.Intent.Logout -> {
                    logoutUseCase()
                    emitEffect(HomeContract.Effect.NavigateToLogin)
                }
                HomeContract.Intent.LoadCurrentUserProfile -> {
                    loadCurrentUserProfile()
                }
            }
        }
    }

    private fun loadCurrentUserProfile() {
        viewModelScope.launch {
            setState { copy(isLoadingProfile = true) }
            getProfileUseCase()
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
