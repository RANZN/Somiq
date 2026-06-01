package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import androidx.navigation3.ui.NavDisplay
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberAppNavBackStack(AppNavGraph.Splash)
    val snackbarHostState = LocalSnackbar.current

    val isHomeOnTop = backStack.isHomeOnTop()

    CompositionLocalProvider(LocalNavBackStack provides backStack) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = {
                if (!isHomeOnTop) {
                    SnackbarHost(snackbarHostState)
                }
            },
        ) {
            NavDisplay(
                modifier = Modifier.fillMaxSize(),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = koinEntryProvider()
            )
        }
    }
}