package com.ranjan.somiq

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ranjan.somiq.core.presentation.component.AppScaffold
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.theme.MyApplicationTheme
import com.ranjan.somiq.navigation.AppNavigation
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val globalEffectDispatcher: GlobalEffectDispatcher = koinInject()
    MyApplicationTheme {
        AppScaffold(
            globalEffectDispatcher = globalEffectDispatcher,
            content = { AppNavigation() }
        )
    }
}