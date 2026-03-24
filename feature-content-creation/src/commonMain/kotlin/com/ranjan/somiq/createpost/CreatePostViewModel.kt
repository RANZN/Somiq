package com.ranjan.somiq.createpost

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.platform.readUriToBytes
import com.ranjan.somiq.feed.data.model.CreatePostRequest
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.feed.domain.usecase.CreatePostUseCase
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val feedRepository: FeedRepository
) : BaseViewModel<CreatePostContract.UiState, CreatePostContract.Intent, CreatePostContract.Effect>(
    CreatePostContract.UiState()
) {

    override fun onIntent(intent: CreatePostContract.Intent) {
        when (intent) {
            is CreatePostContract.Intent.CaptionChange -> setState { copy(caption = intent.value, error = null) }
            is CreatePostContract.Intent.ImagePicked -> setState { copy(selectedImageUri = intent.uri, error = null) }
            is CreatePostContract.Intent.ClearError -> setState { copy(error = null) }
            is CreatePostContract.Intent.Post -> post()
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun post() {
        val uri = state.value.selectedImageUri
        val caption = state.value.caption.trim()
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
            val fileName = "post_${Clock.System.now().toEpochMilliseconds()}.jpg"
            feedRepository.uploadImage(bytes, fileName).fold(
                onSuccess = { imageUrl ->
                    val request = CreatePostRequest(
                        title = caption.take(100).ifBlank { "Post" },
                        content = caption,
                        mediaUrls = listOf(imageUrl)
                    )
                    createPostUseCase(request).fold(
                        onSuccess = {
                            setState { copy(isLoading = false) }
                            emitEffect(CreatePostContract.Effect.PostSuccess)
                        },
                        onFailure = { e ->
                            setState {
                                copy(
                                    isLoading = false,
                                    error = e.message ?: "Failed to create post"
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
