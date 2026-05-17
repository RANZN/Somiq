package com.ranjan.somiq.core.presentation.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalSnackbar = compositionLocalOf<SnackbarHostState> {
    error("LocalSnackbar not provided")
}
