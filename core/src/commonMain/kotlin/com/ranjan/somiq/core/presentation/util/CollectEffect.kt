package com.ranjan.somiq.core.presentation.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ranjan.somiq.core.presentation.model.AppSnackbarDuration
import com.ranjan.somiq.core.presentation.model.resolve
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel

/**
 * Extension function on [BaseViewModel] to collect both screen-specific and common UI effects.
 */
@Suppress("ComposableNaming")
@Composable
fun <E : BaseUiEffect> BaseViewModel<*, E>.collectEffects(
    onEffect: suspend (E) -> Unit
) {
    val snackbarHostState = LocalSnackbar.current
    LaunchedEffect(effect) {
        effect.collect(onEffect)
    }
    LaunchedEffect(commonEffect) {
        commonEffect.collect { effect ->
            when (effect) {
                is BaseUiEffect.Common.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = effect.message.resolve(),
                        actionLabel = effect.actionLabel,
                        withDismissAction = effect.withDismissAction,
                        duration = effect.duration.toComposeDuration(effect.actionLabel)
                    )
                }
            }
        }
    }
}

/**
 * Extension to map platform-agnostic [AppSnackbarDuration] to Compose's [SnackbarDuration].
 */
fun AppSnackbarDuration?.toComposeDuration(actionLabel: String?): SnackbarDuration {
    val finalDuration = this ?: AppSnackbarDuration.defaultFor(actionLabel)
    return when (finalDuration) {
        AppSnackbarDuration.SHORT -> SnackbarDuration.Short
        AppSnackbarDuration.LONG -> SnackbarDuration.Long
        AppSnackbarDuration.INDEFINITE -> SnackbarDuration.Indefinite
    }
}
