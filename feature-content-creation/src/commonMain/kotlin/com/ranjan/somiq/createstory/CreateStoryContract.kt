package com.ranjan.somiq.createstory

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

interface CreateStoryContract {
    data class UiState(
        val selectedImageUri: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null
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
