package com.ranjan.somiq.profile.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import com.ranjan.somiq.profile.ui.ProfileContract.Effect
import com.ranjan.somiq.profile.ui.ProfileContract.Intent
import com.ranjan.somiq.profile.ui.ProfileContract.UiState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val feedRepository: FeedRepository
) : BaseViewModel<UiState, Intent, Effect>() {

    override val initialState: UiState get() = UiState()

    private var userId: String? = null

    fun setUserId(userId: String?) {
        this.userId = userId
    }

    init {
        handleIntent(Intent.LoadProfile)
    }

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.LoadProfile -> loadProfile()
                is Intent.RefreshProfile -> refreshProfile()
                is Intent.SetAppBarConfig -> setState { copy(showAppBar = intent.show, appBarTitle = intent.title) }
                is Intent.SelectTab -> setState { copy(selectedTab = intent.tab) }
                is Intent.OnLogoutClick -> emitEffect(Effect.Logout)
                is Intent.ClearError -> setState { copy(error = null) }
                is Intent.Retry -> {
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
            if (userId == null) {
                loadOwnPostsStoriesAndSaved(profile.user.id)
            } else {
                loadUserPosts(profile.user.id)
            }
        }
    }

    private suspend fun loadUserPosts(profileUserId: String) {
        feedRepository.getPostsByUser(profileUserId).getOrElse { emptyList() }.let { posts ->
            setState { copy(myPosts = posts) }
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
            if (userId == null) {
                loadOwnPostsStoriesAndSaved(profile.user.id)
            } else {
                loadUserPosts(profile.user.id)
            }
        }
    }

    private suspend fun loadOwnPostsStoriesAndSaved(profileUserId: String) {
        feedRepository.getPostsByUser(profileUserId).getOrElse { emptyList() }.let { posts ->
            setState { copy(myPosts = posts) }
        }
        feedRepository.getMyStories().getOrElse { emptyList() }.let { stories ->
            setState { copy(myStories = stories) }
        }
        feedRepository.getBookmarkedPosts().getOrElse { emptyList() }.let { saved ->
            setState { copy(savedPosts = saved) }
        }
    }
}
