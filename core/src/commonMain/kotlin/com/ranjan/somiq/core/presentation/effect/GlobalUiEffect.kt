package com.ranjan.somiq.core.presentation.effect

import androidx.compose.material3.SnackbarDuration
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect

sealed interface GlobalUiEffect : BaseUiEffect {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : GlobalUiEffect
}