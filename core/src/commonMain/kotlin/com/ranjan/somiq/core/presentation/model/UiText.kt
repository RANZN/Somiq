package com.ranjan.somiq.core.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class UiText {
    data class Dynamic(val value: String) : UiText()
    data class Resource(val resId: StringResource) : UiText()
}

@Composable
fun UiText.asString() = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> stringResource(resId)
}
