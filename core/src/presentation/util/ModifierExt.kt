package com.ranjan.somiq.core.presentation.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.core.presentation.component.OnClick

@Stable
fun Modifier.defaultHorizontalPadding() = padding(horizontal = 16.dp)

@Stable
fun Modifier.defaultPadding() = padding(16.dp)

@Composable
@Stable
fun Modifier.screenDefault() = padding(16.dp).verticalScroll(rememberScrollState())

@Stable
fun Modifier.clickWithEffect(enabled: Boolean = true, click: OnClick) = padding(horizontal = 5.dp)
    .clip(RoundedCornerShape(10.dp))
    .clickable(enabled = enabled) { click() }