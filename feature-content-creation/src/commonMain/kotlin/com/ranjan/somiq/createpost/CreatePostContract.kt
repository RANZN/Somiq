package com.ranjan.somiq.createpost

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

interface CreatePostContract {
    data class UiState(
        val caption: String = "",
        val selectedImageUri: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : BaseUiState

    sealed interface Intent : BaseUiIntent {
        data class CaptionChange(val value: String) : Intent
        data class ImagePicked(val uri: String) : Intent
        data object Post : Intent
        data object ClearError : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object PostSuccess : Effect
    }
}
