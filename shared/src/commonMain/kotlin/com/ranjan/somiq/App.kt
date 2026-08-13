package com.ranjan.somiq

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.ranjan.somiq.core.di.InitializeCoil
import com.ranjan.somiq.core.di.platformModules
import com.ranjan.somiq.core.presentation.snackbar.CollectGlobalUiEffects
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.di.sharedModules
import com.ranjan.somiq.navigation.AppNavigation
import com.ranjan.somiq.presentation.theme.MyApplicationTheme
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App(koinConfig: (KoinApplication.() -> Unit) = {}) {
    KoinApplication(
        configuration = koinConfiguration {
            koinConfig()
            modules(platformModules)
            modules(sharedModules)
        }
    ) {
        InitializeCoil()
        MyApplicationTheme {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(LocalSnackbar provides snackbarHostState) {
                CollectGlobalUiEffects()
                AppNavigation()
            }
        }
    }
}
