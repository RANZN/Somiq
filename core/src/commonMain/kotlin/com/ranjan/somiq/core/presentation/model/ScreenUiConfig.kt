package com.ranjan.somiq.core.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class ScreenUiConfig(
    val topBar: (@Composable () -> Unit)? = null,
    val fab: (@Composable () -> Unit)? = null,
)
