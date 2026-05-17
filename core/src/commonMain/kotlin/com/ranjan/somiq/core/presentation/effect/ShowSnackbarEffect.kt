package com.ranjan.somiq.core.presentation.effect

import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect

interface ShowSnackbarEffect : BaseUiEffect {
    val message: UiText
}
