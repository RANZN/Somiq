package com.ranjan.somiq.feed.ui.storyview

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import kotlinx.coroutines.launch

class StoryViewViewModel(
    private val storyId: String,
    private val feedRepository: FeedRepository
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
            feedRepository.getStory(storyId).fold(
                onSuccess = { story ->
                    setState { copy(story = story, isLoading = false, error = null) }
                },
                onFailure = { e ->
                    setState {
                        copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load story"
                        )
                    }
                }
            )
        }
    }
}
