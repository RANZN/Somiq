package com.ranjan.somiq.core.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.getString

sealed class UiText {
    data class Dynamic(val value: String) : UiText()
    data class Resource(
        val resId: StringResource,
        val args: List<Any> = emptyList()
    ) : UiText()
}

@Composable
fun UiText.asString() = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> stringResource(resId)
}

suspend fun UiText.resolve() = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> getString(resId, *args.toTypedArray())
}