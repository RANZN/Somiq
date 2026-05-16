package com.ranjan.somiq

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.navigation.AppNavigation
import com.ranjan.somiq.presentation.theme.MyApplicationTheme
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }
    val globalEffectDispatcher: GlobalEffectDispatcher = koinInject()

    CollectEffect(globalEffectDispatcher.effects) { effect ->
        when (effect) {
            is GlobalUiEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = effect.message,
                    actionLabel = effect.actionLabel,
                    duration = effect.duration
                )
            }
        }
    }

    MyApplicationTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = {
                AppNavigation()
            }
        )
    }
}