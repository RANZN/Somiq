package com.ranjan.somiq.createpost

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.domain.PostUploadService
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.platform.MediaPicker
import com.ranjan.somiq.core.platform.MediaType
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val postUploadManager: PostUploadService,
    private val mediaPicker: MediaPicker
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

            is CreatePostContract.Intent.ImagesPicked -> setState {
                copy(
                    selectedImageUris = selectedImageUris + intent.uris,
                    error = null
                )
            }

            is CreatePostContract.Intent.RemoveImage -> setState {
                val updatedUris = selectedImageUris.toMutableList().apply {
                    if (intent.index in indices) {
                        removeAt(intent.index)
                    }
                }
                copy(
                    selectedImageUris = updatedUris,
                    error = null
                )
            }

            is CreatePostContract.Intent.ClearError -> setState { copy(error = null) }
            
            CreatePostContract.Intent.PickImage -> {
                viewModelScope.launch {
                    val uri = mediaPicker.pickMedia(MediaType.IMAGE)
                    if (uri != null) {
                        onIntent(CreatePostContract.Intent.ImagesPicked(listOf(uri)))
                    }
                }
            }

            CreatePostContract.Intent.Post -> post()
        }
    }

    private fun post() {
        val uris = state.value.selectedImageUris
        val caption = state.value.caption.trim()
        if (uris.isEmpty()) {
            val appError = AppError.Custom(CreatePostContract.ScreenError.PleaseSelectImage)
            setState { copy(error = appError) }
            showSnackbar(appError)
            return
        }

        // Delegate to background upload manager and trigger success effect immediately
        postUploadManager.uploadPost(caption, uris)
        viewModelScope.launch {
            emitEffect(CreatePostContract.Effect.PostSuccess)
        }
    }
}
