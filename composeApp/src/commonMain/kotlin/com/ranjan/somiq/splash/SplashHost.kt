package com.ranjan.somiq.splash

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreenHost(
    viewmodel: SplashViewModel = koinViewModel(),
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    ObserveAsEvent(viewmodel.splashAction) {
        when (it) {
            is SplashEvents.NavigateToHome -> navigateToHome()
            is SplashEvents.NavigateToLogin -> navigateToLogin()
        }
    }

    SplashScreen()
}