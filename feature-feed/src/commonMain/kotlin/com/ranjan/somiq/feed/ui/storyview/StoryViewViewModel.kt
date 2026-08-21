package com.ranjan.somiq.feed.ui.storyview

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.repository.StoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoryViewViewModel(
    private val storyId: String,
    private val storyRepository: StoryRepository
) : BaseViewModel<StoryViewContract.Intent, StoryViewContract.Effect>() {

    private val _uiState = MutableStateFlow(StoryViewContract.UiState())
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: StoryViewContract.Intent) {}
    init {
        loadStory()
    }
    private fun loadStory() {
        viewModelScope.launch {
            _uiState.update {it.copy(isLoading = true, error = null) }
            storyRepository.getStory(storyId).fold(
                onSuccess = { story ->
                    _uiState.update {it.copy(story = story, isLoading = false, error = null) }
                },
                onFailure = { e ->
                    val appError = e.toAppError(StoryViewContract.ScreenError.LoadStoryFailed)
                    _uiState.update {it.copy(isLoading = false, error = appError) }
                    showSnackbar(appError)
                }
            )
        }
    }
}
