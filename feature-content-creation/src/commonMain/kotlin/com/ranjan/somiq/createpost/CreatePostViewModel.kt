package com.ranjan.somiq.createpost

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.domain.PostUploadService
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val postUploadManager: PostUploadService
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

        // Delegate to background upload manager and trigger success effect immediately
        postUploadManager.uploadPost(caption, uri)
        viewModelScope.launch {
            emitEffect(CreatePostContract.Effect.PostSuccess)
        }
    }
}
