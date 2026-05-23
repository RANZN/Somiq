package com.ranjan.somiq.app.home.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.usecase.LogoutUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<HomeContract.UiState, HomeContract.Intent, HomeContract.Effect>(
    HomeContract.UiState()
) {

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
                    logoutUseCase.invoke()
                    emitEffect(HomeContract.Effect.Logout)
                }

                HomeContract.Intent.LoadCurrentUserProfile -> {
                    loadCurrentUserProfile()
                }

                // Navigation
                is HomeContract.Intent.NavigateToUser -> emitEffect(
                    HomeContract.Effect.NavigateToUser(
                        intent.userId
                    )
                )

                is HomeContract.Intent.NavigateToPost -> emitEffect(
                    HomeContract.Effect.NavigateToPost(
                        intent.postId
                    )
                )

                is HomeContract.Intent.NavigateToComments -> emitEffect(
                    HomeContract.Effect.NavigateToComments(
                        intent.postId
                    )
                )

                is HomeContract.Intent.NavigateToStory -> emitEffect(
                    HomeContract.Effect.NavigateToStory(
                        intent.storyId
                    )
                )

                is HomeContract.Intent.ShowShareDialog -> emitEffect(
                    HomeContract.Effect.ShowShareDialog(
                        intent.postId
                    )
                )

                is HomeContract.Intent.ShowMoreOptions -> emitEffect(
                    HomeContract.Effect.ShowMoreOptions(
                        intent.postId
                    )
                )

                is HomeContract.Intent.NavigateToEditProfile -> emitEffect(
                    HomeContract.Effect.NavigateToEditProfile(
                        intent.userId
                    )
                )

                is HomeContract.Intent.NavigateToSettings -> emitEffect(
                    HomeContract.Effect.NavigateToSettings(
                        intent.userId
                    )
                )

                is HomeContract.Intent.NavigateToFollowers -> emitEffect(
                    HomeContract.Effect.NavigateToFollowers(
                        intent.userId
                    )
                )

                is HomeContract.Intent.NavigateToFollowing -> emitEffect(
                    HomeContract.Effect.NavigateToFollowing(
                        intent.userId
                    )
                )

                is HomeContract.Intent.NavigateToConversation -> emitEffect(
                    HomeContract.Effect.NavigateToConversation(
                        intent.userId
                    )
                )

                HomeContract.Intent.NavigateToNotifications -> emitEffect(HomeContract.Effect.NavigateToNotifications)
                HomeContract.Intent.NavigateToCreatePost -> emitEffect(HomeContract.Effect.NavigateToCreatePost)
                HomeContract.Intent.NavigateToCreateStory -> emitEffect(HomeContract.Effect.NavigateToCreateStory)
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
