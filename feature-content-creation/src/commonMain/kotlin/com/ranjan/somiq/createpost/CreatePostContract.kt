package com.ranjan.somiq.createpost

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_could_not_read_image
import com.ranjan.somiq.core.resources.error_failed_to_create_post
import com.ranjan.somiq.core.resources.error_failed_to_upload_image
import com.ranjan.somiq.core.resources.error_please_select_image

interface CreatePostContract {
    sealed class ScreenError : BaseScreenError {
        data object PleaseSelectImage : ScreenError()
        data object CouldNotReadImage : ScreenError()
        data object CreatePostFailed : ScreenError()
        data object UploadImageFailed : ScreenError()

        override fun toUiText(): UiText = when (this) {
            PleaseSelectImage -> UiText.Resource(Res.string.error_please_select_image)
            CouldNotReadImage -> UiText.Resource(Res.string.error_could_not_read_image)
            CreatePostFailed -> UiText.Resource(Res.string.error_failed_to_create_post)
            UploadImageFailed -> UiText.Resource(Res.string.error_failed_to_upload_image)
        }
    }

    @Stable
    data class UiState(
        val caption: String = "",
        val selectedImageUris: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val error: AppError? = null
    ) : BaseUiState

    sealed interface Intent : BaseUiIntent {
        data class CaptionChange(val value: String) : Intent
        data class ImagesPicked(val uris: List<String>) : Intent
        data class RemoveImage(val index: Int) : Intent
        data object Post : Intent
        data object ClearError : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object PostSuccess : Effect
    }
}
