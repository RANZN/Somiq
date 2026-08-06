package com.ranjan.somiq.createpost

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.platform.readUriToBytes
import com.ranjan.somiq.feed.data.model.CreatePostRequest
import com.ranjan.somiq.feed.data.model.PostMedia
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.feed.domain.usecase.CreatePostUseCase
import kotlinx.coroutines.launch
import kotlin.time.Clock

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val feedRepository: FeedRepository
) : BaseViewModel<CreatePostContract.UiState, CreatePostContract.Intent, CreatePostContract.Effect>(
    CreatePostContract.UiState()
) {

    override fun onIntent(intent: CreatePostContract.Intent) {
        when (intent) {
            is CreatePostContract.Intent.CaptionChange -> setState {
                copy(
                    caption = intent.value,
                    error = null
                )
            }

            is CreatePostContract.Intent.ImagePicked -> setState {
                copy(
                    selectedImageUri = intent.uri,
                    error = null
                )
            }

            is CreatePostContract.Intent.ClearError -> setState { copy(error = null) }
            is CreatePostContract.Intent.Post -> post()
        }
    }

    private fun post() {
        val uri = state.value.selectedImageUri
        val caption = state.value.caption.trim()
        if (uri.isNullOrBlank()) {
            val appError = AppError.Custom(CreatePostContract.ScreenError.PleaseSelectImage)
            setState { copy(error = appError) }
            showSnackbar(appError)
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            val bytes = readUriToBytes(uri)
            if (bytes == null || bytes.isEmpty()) {
                val appError = AppError.Custom(CreatePostContract.ScreenError.CouldNotReadImage)
                setState { copy(isLoading = false, error = appError) }
                showSnackbar(appError)
                return@launch
            }
            val fileName = "post_${Clock.System.now().toEpochMilliseconds()}"

            val request = CreatePostRequest(
                caption = caption,
                mediaUrls = listOf(
                    PostMedia(
                        name = fileName,
                        byte = bytes
                    )
                ),
            )

            createPostUseCase(request).fold(
                onSuccess = {
                    setState { copy(isLoading = false) }
                    emitEffect(CreatePostContract.Effect.PostSuccess)
                },
                onFailure = { e ->
                    val appError = e.toAppError(CreatePostContract.ScreenError.CreatePostFailed)
                    setState { copy(isLoading = false, error = appError) }
                    showSnackbar(appError)
                }
            )
        }
    }
}
