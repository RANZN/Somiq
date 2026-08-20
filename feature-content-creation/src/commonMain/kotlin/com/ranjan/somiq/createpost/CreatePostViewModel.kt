package com.ranjan.somiq.createpost

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.domain.PostUploadService
import com.ranjan.somiq.core.platform.MediaPicker
import com.ranjan.somiq.core.platform.MediaType
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val postUploadManager: PostUploadService,
    private val mediaPicker: MediaPicker
) : BaseViewModel<CreatePostContract.Intent, CreatePostContract.Effect>() {

    private val _uiState = MutableStateFlow(CreatePostContract.UiState())
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: CreatePostContract.Intent) {
        when (intent) {
            is CreatePostContract.Intent.CaptionChange -> _uiState.update {it.copy(
                    caption = intent.value,
                    error = null
                )
            }
            is CreatePostContract.Intent.ImagesPicked -> _uiState.update { it.copy(
                    selectedImageUris = it.selectedImageUris + intent.uris,
                    error = null
                )
            }
            is CreatePostContract.Intent.RemoveImage -> _uiState.update {
                val updatedUris = it.selectedImageUris.toMutableList().apply {
                    if (intent.index in indices) {
                        removeAt(intent.index)
                    }
                }
                it.copy(
                    selectedImageUris = updatedUris,
                    error = null
                )
            }
            is CreatePostContract.Intent.ClearError -> _uiState.update {it.copy(error = null) }
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
        val uris = uiState.value.selectedImageUris
        val caption = uiState.value.caption.trim()
        if (uris.isEmpty()) {
            val appError = AppError.Custom(CreatePostContract.ScreenError.PleaseSelectImage)
            _uiState.update {it.copy(error = appError) }
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
