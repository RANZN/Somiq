package com.ranjan.somiq.createstory

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.platform.readUriToBytes
import com.ranjan.somiq.feed.data.model.CreateStoryRequest
import com.ranjan.somiq.feed.data.model.MediaType
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CreateStoryViewModel(
    private val feedRepository: FeedRepository
) : BaseViewModel<CreateStoryContract.UiState, CreateStoryContract.Intent, CreateStoryContract.Effect>() {

    override val initialState: CreateStoryContract.UiState get() = CreateStoryContract.UiState()

    override fun onIntent(intent: CreateStoryContract.Intent) {
        when (intent) {
            is CreateStoryContract.Intent.ImagePicked -> setState { copy(selectedImageUri = intent.uri, error = null) }
            is CreateStoryContract.Intent.ClearError -> setState { copy(error = null) }
            is CreateStoryContract.Intent.Post -> post()
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun post() {
        val uri = state.value.selectedImageUri
        if (uri.isNullOrBlank()) {
            setState { copy(error = "Please select an image") }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            val bytes = readUriToBytes(uri)
            if (bytes == null || bytes.isEmpty()) {
                setState { copy(isLoading = false, error = "Could not read image") }
                return@launch
            }
            val fileName = "story_${Clock.System.now()}.jpg"
            feedRepository.uploadImage(bytes, fileName).fold(
                onSuccess = { mediaUrl ->
                    val request = CreateStoryRequest(mediaUrl = mediaUrl, mediaType = MediaType.IMAGE)
                    feedRepository.createStory(request).fold(
                        onSuccess = {
                            setState { copy(isLoading = false) }
                            emitEffect(CreateStoryContract.Effect.StorySuccess)
                        },
                        onFailure = { e ->
                            setState {
                                copy(
                                    isLoading = false,
                                    error = e.message ?: "Failed to create story"
                                )
                            }
                        }
                    )
                },
                onFailure = { e ->
                    setState {
                        copy(
                            isLoading = false,
                            error = e.message ?: "Failed to upload image"
                        )
                    }
                }
            )
        }
    }
}
