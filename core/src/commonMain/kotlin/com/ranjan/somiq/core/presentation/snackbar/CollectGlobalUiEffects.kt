package com.ranjan.somiq.core.presentation.snackbar

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.model.resolve
import com.ranjan.somiq.core.presentation.util.CollectEffect

@Composable
fun CollectGlobalUiEffects() {
    val snackbarHostState = LocalSnackbar.current
    CollectEffect(GlobalEffectDispatcher.effects) { effect ->
        when (effect) {
            is GlobalUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(
                message = effect.message.resolve(),
                actionLabel = effect.actionLabel,
                withDismissAction = effect.withDismissAction,
                duration = effect.duration,
            )
        }
    }
}