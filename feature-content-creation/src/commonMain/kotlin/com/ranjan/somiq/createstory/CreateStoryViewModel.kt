package com.ranjan.somiq.createstory

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.platform.readUriToBytes
import com.ranjan.somiq.feed.domain.model.CreateStoryRequest
import com.ranjan.somiq.feed.domain.model.MediaType
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.feed.domain.repository.StoryRepository
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CreateStoryViewModel(
    private val feedRepository: FeedRepository,
    private val storyRepository: StoryRepository
) : BaseViewModel<CreateStoryContract.UiState, CreateStoryContract.Intent, CreateStoryContract.Effect>(
    CreateStoryContract.UiState()
) {

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
            val appError = AppError.Custom(CreateStoryContract.ScreenError.PleaseSelectImage)
            setState { copy(error = appError) }
            showSnackbar(appError)
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            val bytes = readUriToBytes(uri)
            if (bytes == null || bytes.isEmpty()) {
                val appError = AppError.Custom(CreateStoryContract.ScreenError.CouldNotReadImage)
                setState { copy(isLoading = false, error = appError) }
                showSnackbar(appError)
                return@launch
            }
            val fileName = "story_${Clock.System.now()}.jpg"
            feedRepository.uploadImage(bytes, fileName).fold(
                onSuccess = { mediaUrl ->
                    val request = CreateStoryRequest(mediaUrl = mediaUrl, mediaType = MediaType.IMAGE)
                    storyRepository.createStory(request).fold(
                        onSuccess = {
                            setState { copy(isLoading = false) }
                            emitEffect(CreateStoryContract.Effect.StorySuccess)
                        },
                        onFailure = { e ->
                            val appError = e.toAppError(CreateStoryContract.ScreenError.CreateStoryFailed)
                            setState { copy(isLoading = false, error = appError) }
                            showSnackbar(appError)
                        }
                    )
                },
                onFailure = { e ->
                    val appError = e.toAppError(CreateStoryContract.ScreenError.UploadImageFailed)
                    setState { copy(isLoading = false, error = appError) }
                    showSnackbar(appError)
                }
            )
        }
    }
}
