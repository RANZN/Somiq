package com.ranjan.somiq.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.splash.SplashScreenHost
import org.koin.compose.koinInject

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberAppNavBackStack()

    val authStateManager = koinInject<AuthStateManager>()
    val userId by authStateManager.userId.collectAsStateWithLifecycle(initialValue = null)

    NavDisplay(
        modifier = modifier,
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

            key(userId) {
                homeEntries(backStack)
            }
        },
    )
}
