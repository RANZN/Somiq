package com.ranjan.somiq.createstory

import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_could_not_read_image
import com.ranjan.somiq.core.resources.error_failed_to_create_story
import com.ranjan.somiq.core.resources.error_failed_to_upload_image
import com.ranjan.somiq.core.resources.error_please_select_image

interface CreateStoryContract {
    sealed class ScreenError : BaseScreenError {
        data object PleaseSelectImage : ScreenError()
        data object CouldNotReadImage : ScreenError()
        data object CreateStoryFailed : ScreenError()
        data object UploadImageFailed : ScreenError()

        override fun toUiText(): UiText? = when (this) {
            PleaseSelectImage -> UiText.Resource(Res.string.error_please_select_image)
            CouldNotReadImage -> UiText.Resource(Res.string.error_could_not_read_image)
            CreateStoryFailed -> UiText.Resource(Res.string.error_failed_to_create_story)
            UploadImageFailed -> UiText.Resource(Res.string.error_failed_to_upload_image)
        }
    }

    data class UiState(
        val selectedImageUri: String? = null,
        val isLoading: Boolean = false,
        val error: AppError? = null
    ) : BaseUiState

    sealed interface Intent : BaseUiIntent {
        data class ImagePicked(val uri: String) : Intent
        data object Post : Intent
        data object ClearError : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object StorySuccess : Effect
    }
}
