package com.ranjan.somiq.profile.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.usecase.GetBookmarkedPostsUseCase
import com.ranjan.somiq.feed.domain.usecase.GetMyStoriesUseCase
import com.ranjan.somiq.feed.domain.usecase.GetPostsByUserUseCase
import com.ranjan.somiq.profile.domain.usecase.GetProfileUseCase
import com.ranjan.somiq.profile.domain.usecase.LoadOwnProfileUseCase
import com.ranjan.somiq.profile.ui.ProfileContract.Effect
import com.ranjan.somiq.profile.ui.ProfileContract.Intent
import com.ranjan.somiq.profile.ui.ProfileContract.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getPostsByUserUseCase: GetPostsByUserUseCase,
    private val getMyStoriesUseCase: GetMyStoriesUseCase,
    private val getBookmarkedPostsUseCase: GetBookmarkedPostsUseCase,
    private val loadOwnProfileUseCase: LoadOwnProfileUseCase
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private var userId: String? = null
    init {
        observeOwnPostsAndSaved()
    }
    private fun observeOwnPostsAndSaved() {
        viewModelScope.launch {
            getPostsByUserUseCase.myPostsFlow.collect { posts ->
                _uiState.update {it.copy(myPosts = posts) }
            }
        }
        viewModelScope.launch {
            getBookmarkedPostsUseCase.bookmarkedPostsFlow.collect { saved ->
                _uiState.update {it.copy(savedPosts = saved) }
            }
        }
    }
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.LoadProfile -> loadProfile(intent.userId)
                is Intent.RefreshProfile -> refreshProfile()
                is Intent.SetAppBarConfig -> _uiState.update {it.copy(showAppBar = intent.show, appBarTitle = intent.title) }
                is Intent.SelectTab -> _uiState.update {it.copy(selectedTab = intent.tab) }
                is Intent.ClearError -> _uiState.update {it.copy(error = null) }
                is Intent.Retry -> {
                    _uiState.update {it.copy(error = null) }
                    loadProfile()
                }
                Intent.Setting -> emitEffect(Effect.NavigateToSettings)
            }
        }
    }
    private suspend fun loadProfile(userId: String? = this.userId) {
        this.userId = userId
        _uiState.update {it.copy(isLoading = true, error = null) }
        if (userId == null) {
            val result = loadOwnProfileUseCase()
            if (result.isSuccess) {
                val profile = result.getOrThrow()
                _uiState.update {it.copy(
                        profile = profile,
                        isLoading = false,
                        error = null
                    )
                }
                getMyStoriesUseCase().getOrElse { emptyList() }.let { stories ->
                    _uiState.update {it.copy(myStories = stories) }
                }
            } else {
                val error = result.exceptionOrNull() ?: Exception("Unknown error")
                _uiState.update {it.copy(
                        isLoading = false,
                        error = error.toAppError(ProfileContract.ScreenError.LoadProfileFailed)
                    )
                }
            }
        } else {
            val result = getProfileUseCase(userId)
            if (result.isSuccess) {
                val profile = result.getOrThrow()
                _uiState.update {it.copy(
                        profile = profile,
                        isLoading = false,
                        error = null
                    )
                }
                loadUserPosts(profile.user.id)
            } else {
                val error = result.exceptionOrNull() ?: Exception("Unknown error")
                _uiState.update {it.copy(
                        isLoading = false,
                        error = error.toAppError(ProfileContract.ScreenError.LoadProfileFailed)
                    )
                }
            }
        }
    }
    private suspend fun loadUserPosts(profileUserId: String) {
        getPostsByUserUseCase(profileUserId).getOrElse { emptyList() }.let { posts ->
            _uiState.update {it.copy(myPosts = posts) }
        }
    }
    private suspend fun refreshProfile() {
        _uiState.update {it.copy(refreshing = true, error = null) }
        if (userId == null) {
            val result = loadOwnProfileUseCase()
            if (result.isSuccess) {
                val profile = result.getOrThrow()
                _uiState.update {it.copy(
                        profile = profile,
                        refreshing = false,
                        error = null
                    )
                }
                getMyStoriesUseCase().getOrElse { emptyList() }.let { stories ->
                    _uiState.update {it.copy(myStories = stories) }
                }
            } else {
                val error = result.exceptionOrNull() ?: Exception("Unknown error")
                val appError = error.toAppError(ProfileContract.ScreenError.RefreshProfileFailed)
                _uiState.update {it.copy(refreshing = false, error = appError) }
                showSnackbar(appError)
            }
        } else {
            val result = getProfileUseCase(userId)
            if (result.isSuccess) {
                val profile = result.getOrThrow()
                _uiState.update {it.copy(
                        profile = profile,
                        refreshing = false,
                        error = null
                    )
                }
                loadUserPosts(profile.user.id)
            } else {
                val error = result.exceptionOrNull() ?: Exception("Unknown error")
                val appError = error.toAppError(ProfileContract.ScreenError.RefreshProfileFailed)
                _uiState.update {it.copy(refreshing = false, error = appError) }
                showSnackbar(appError)
            }
        }
    }
}
