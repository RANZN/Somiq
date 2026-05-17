package com.ranjan.somiq.core.presentation.effect

import androidx.compose.material3.SnackbarDuration
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect

sealed interface GlobalUiEffect : BaseUiEffect {
    data class ShowSnackbar(
        val message: UiText,
        val actionLabel: String? = null,
        val withDismissAction: Boolean = false,
        val duration: SnackbarDuration = if (actionLabel == null) {
            SnackbarDuration.Short
        } else {
            SnackbarDuration.Indefinite
        },
    ) : GlobalUiEffect

    companion object {
        fun showSnackbar(
            message: UiText,
            actionLabel: String? = null,
            withDismissAction: Boolean = false,
            duration: SnackbarDuration? = null,
        ): ShowSnackbar = ShowSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration ?: if (actionLabel == null) {
                SnackbarDuration.Short
            } else {
                SnackbarDuration.Indefinite
            },
        )
    }
}
