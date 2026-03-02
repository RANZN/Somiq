package com.ranjan.somiq.core.presentation.effect

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect

sealed interface GlobalUiEffect : BaseUiEffect {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : GlobalUiEffect
}

enum class SnackbarDuration {
    Short,
    Long,
    Indefinite
}
