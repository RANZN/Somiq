package com.ranjan.somiq.core.presentation.effect

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect

sealed interface GlobalUiEffect : BaseUiEffect {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
    ) : GlobalUiEffect
}