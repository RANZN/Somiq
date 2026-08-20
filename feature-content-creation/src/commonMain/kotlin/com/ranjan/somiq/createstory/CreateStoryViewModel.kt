package com.ranjan.somiq.createstory

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.platform.MediaPicker
import com.ranjan.somiq.core.platform.MediaType as PickerMediaType
import com.ranjan.somiq.core.platform.readUriToBytes
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.model.CreateStoryRequest
import com.ranjan.somiq.feed.domain.model.MediaType
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.feed.domain.repository.StoryRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateStoryViewModel(
    private val feedRepository: FeedRepository,
    private val storyRepository: StoryRepository,
    private val mediaPicker: MediaPicker
) : BaseViewModel<CreateStoryContract.Intent, CreateStoryContract.Effect>() {

    private val _uiState = MutableStateFlow(CreateStoryContract.UiState())
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: CreateStoryContract.Intent) {
        when (intent) {
            is CreateStoryContract.Intent.ImagePicked -> _uiState.update {it.copy(selectedImageUri = intent.uri, error = null) }
            is CreateStoryContract.Intent.ClearError -> _uiState.update {it.copy(error = null) }
            CreateStoryContract.Intent.PickImage -> {
                viewModelScope.launch {
                    val uri = mediaPicker.pickMedia(PickerMediaType.IMAGE)
                    if (uri != null) {
                        onIntent(CreateStoryContract.Intent.ImagePicked(uri))
                    }
                }
            }
            CreateStoryContract.Intent.Post -> post()
        }
    }
    @OptIn(ExperimentalTime::class)
    private fun post() {
        val uri = uiState.value.selectedImageUri
        if (uri.isNullOrBlank()) {
            val appError = AppError.Custom(CreateStoryContract.ScreenError.PleaseSelectImage)
            _uiState.update {it.copy(error = appError) }
            showSnackbar(appError)
            return
        }
        viewModelScope.launch {
            _uiState.update {it.copy(isLoading = true, error = null) }
            val bytes = readUriToBytes(uri)
            if (bytes == null || bytes.isEmpty()) {
                val appError = AppError.Custom(CreateStoryContract.ScreenError.CouldNotReadImage)
                _uiState.update {it.copy(isLoading = false, error = appError) }
                showSnackbar(appError)
                return@launch
            }
            val fileName = "story_${Clock.System.now()}.jpg"
            feedRepository.uploadImage(bytes, fileName).fold(
                onSuccess = { mediaUrl ->
                    val request = CreateStoryRequest(mediaUrl = mediaUrl, mediaType = MediaType.IMAGE)
                    storyRepository.createStory(request).fold(
                        onSuccess = {
                            _uiState.update {it.copy(isLoading = false) }
                            emitEffect(CreateStoryContract.Effect.StorySuccess)
                        },
                        onFailure = { e ->
                            val appError = e.toAppError(CreateStoryContract.ScreenError.CreateStoryFailed)
                            _uiState.update {it.copy(isLoading = false, error = appError) }
                            showSnackbar(appError)
                        }
                    )
                },
                onFailure = { e ->
                    val appError = e.toAppError(CreateStoryContract.ScreenError.UploadImageFailed)
                    _uiState.update {it.copy(isLoading = false, error = appError) }
                    showSnackbar(appError)
                }
            )
        }
    }
}
