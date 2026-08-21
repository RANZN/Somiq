package com.ranjan.somiq.core.presentation.effect

import com.ranjan.somiq.core.presentation.model.AppSnackbarDuration
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect

sealed interface GlobalUiEffect : BaseUiEffect {
    data class ShowSnackbar(
        val message: UiText,
        val actionLabel: String? = null,
        val withDismissAction: Boolean = false,
        val duration: AppSnackbarDuration = AppSnackbarDuration.defaultFor(actionLabel),
    ) : GlobalUiEffect

    companion object {
        fun showSnackbar(
            message: UiText,
            actionLabel: String? = null,
            withDismissAction: Boolean = false,
            duration: AppSnackbarDuration? = null,
        ): ShowSnackbar = ShowSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration ?: AppSnackbarDuration.defaultFor(actionLabel),
        )
    }
}
