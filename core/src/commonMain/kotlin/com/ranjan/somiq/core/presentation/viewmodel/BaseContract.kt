package com.ranjan.somiq.core.presentation.viewmodel

import androidx.compose.runtime.Immutable

import androidx.compose.material3.SnackbarDuration
import com.ranjan.somiq.core.presentation.model.UiText

/**
 * Base contract interfaces for UI state management pattern.
 * All ViewModels should use these interfaces for their State, Intent, and Effect types.
 */
@Immutable
interface BaseUiIntent

interface BaseUiEffect {
    sealed interface Common : BaseUiEffect {
        data class ShowSnackbar(
            val message: UiText,
            val actionLabel: String? = null,
            val withDismissAction: Boolean = false,
            val duration: SnackbarDuration? = null
        ) : Common
    }
}

data object NoState

interface NoIntent : BaseUiIntent
