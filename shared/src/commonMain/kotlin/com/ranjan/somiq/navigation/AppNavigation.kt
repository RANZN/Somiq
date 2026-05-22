package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.splash.SplashScreenHost
import org.koin.compose.koinInject
import kotlin.random.Random

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberAppNavBackStack()
    val snackbarHostState = LocalSnackbar.current

    val authStateManager = koinInject<AuthStateManager>()
    val userId by authStateManager.userId.collectAsStateWithLifecycle(initialValue = null)
    val sessionId = remember(userId) { "${userId}-${Random.nextLong()}" }
    val isHomeOnTop = backStack.isHomeOnTop()

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
                homeEntries(backStack, sessionId)
            },
        )
    }
}