package com.ranjan.somiq.core.presentation.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VSpace(dp: Dp) {
    Spacer(Modifier.height(dp))
}

@Composable
fun ColumnScope.FillSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.weight(1f))
}

@Composable
fun RowScope.FillSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.weight(1f))
}