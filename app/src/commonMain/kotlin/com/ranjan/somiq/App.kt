package com.ranjan.somiq

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.di.InitializeCoil
import com.ranjan.somiq.core.presentation.component.AppScaffold
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.theme.MyApplicationTheme
import com.ranjan.somiq.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    InitializeCoil()
    
    val globalEffectDispatcher: GlobalEffectDispatcher = koinInject()
    MyApplicationTheme {
        AppScaffold(globalEffectDispatcher = globalEffectDispatcher) { paddingValues ->
            AppNavigation()
        }
    }
}