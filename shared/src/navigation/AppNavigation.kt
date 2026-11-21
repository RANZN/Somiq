package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.navigation.AppNavGraph.HomeGraph
import com.ranjan.somiq.navigation.AppNavGraph.OnBoarding
import com.ranjan.somiq.navigation.AppNavGraph.Splash
import com.ranjan.somiq.splash.SplashScreenHost

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberAppNavBackStack(Splash)
    val snackbarHostState = LocalSnackbar.current

    val showGlobalSnackbar = when {
        backStack.isHomeOnTop() -> false
        else -> true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            if (showGlobalSnackbar) {
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
            entryProvider = entryProvider {
                entry<Splash> {
                    SplashScreenHost(
                        navigateToHome = {
                            backStack.clear()
                            backStack.add(HomeGraph)
                        },
                        navigateToLogin = {
                            backStack.clear()
                            backStack.add(OnBoarding.Login)
                        },
                    )
                }
                authEntries(backStack)
                homeEntries(backStack)
                settingEntries(backStack)
            }
        )
    }
}