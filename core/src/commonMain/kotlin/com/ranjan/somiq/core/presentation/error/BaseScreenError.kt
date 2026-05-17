package com.ranjan.somiq.core.presentation.error

import com.ranjan.somiq.core.presentation.model.UiText

/** Screen-specific error; each feature defines a sealed `ScreenError` implementation. */
interface BaseScreenError {
    fun toUiText(): UiText
}
