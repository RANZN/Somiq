package com.ranjan.somiq.feed.ui.storyview

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.repository.StoryRepository
import kotlinx.coroutines.launch

class StoryViewViewModel(
    private val storyId: String,
    private val storyRepository: StoryRepository
) : BaseViewModel<StoryViewContract.UiState, StoryViewContract.Intent, StoryViewContract.Effect>(
    StoryViewContract.UiState()
) {

    override fun onIntent(intent: StoryViewContract.Intent) {}

    init {
        loadStory()
    }

    private fun loadStory() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            storyRepository.getStory(storyId).fold(
                onSuccess = { story ->
                    setState { copy(story = story, isLoading = false, error = null) }
                },
                onFailure = { e ->
                    val appError = e.toAppError(StoryViewContract.ScreenError.LoadStoryFailed)
                    setState { copy(isLoading = false, error = appError) }
                    showSnackbar(appError)
                }
            )
        }
    }
}
