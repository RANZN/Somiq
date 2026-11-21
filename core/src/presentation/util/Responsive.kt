package com.ranjan.somiq.core.presentation.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Breakpoints for responsive layout (industry practice).
 * Compact: phone portrait; Medium: phone landscape / small tablet; Expanded: tablet / desktop.
 */
object Responsive {
    val compactMaxWidth: Dp = 480.dp
    val mediumMaxWidth: Dp = 840.dp
    val horizontalPaddingCompact: Dp = 16.dp
    val horizontalPaddingMedium: Dp = 24.dp
    val horizontalPaddingExpanded: Dp = 32.dp
}

/**
 * Horizontal padding that adapts to content width for a responsive feel.
 */
fun responsiveHorizontalPadding(width: Dp): PaddingValues {
    return when {
        width < Responsive.compactMaxWidth -> PaddingValues(horizontal = Responsive.horizontalPaddingCompact)
        width < Responsive.mediumMaxWidth -> PaddingValues(horizontal = Responsive.horizontalPaddingMedium)
        else -> PaddingValues(horizontal = Responsive.horizontalPaddingExpanded)
    }
}
