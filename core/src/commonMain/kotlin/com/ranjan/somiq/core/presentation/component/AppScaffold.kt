package com.ranjan.somiq.core.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration as MaterialSnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.effect.SnackbarDuration
import com.ranjan.somiq.core.presentation.util.CollectEffect

@Composable
fun AppScaffold(
    globalEffectDispatcher: GlobalEffectDispatcher,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    CollectEffect(globalEffectDispatcher.effects) { effect ->
        when (effect) {
            is GlobalUiEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = effect.message,
                    actionLabel = effect.actionLabel,
                    duration = when (effect.duration) {
                        SnackbarDuration.Short -> MaterialSnackbarDuration.Short
                        SnackbarDuration.Long -> MaterialSnackbarDuration.Long
                        SnackbarDuration.Indefinite -> MaterialSnackbarDuration.Indefinite
                    }
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        content(paddingValues)
    }
}
