package com.ranjan.somiq.core.presentation.model

enum class AppSnackbarDuration {
    SHORT,
    LONG,
    INDEFINITE;

    companion object {
        fun defaultFor(actionLabel: String?): AppSnackbarDuration {
            return if (actionLabel == null) SHORT else INDEFINITE
        }
    }
}
