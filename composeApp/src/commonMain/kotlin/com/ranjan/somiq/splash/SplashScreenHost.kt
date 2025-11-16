package com.ranjan.somiq.splash

import androidx.compose.runtime.Composable
import com.ranjan.somiq.util.ObserveAsEvent
import com.ranjan.somiq.presentation.splash.SplashViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun SplashScreenHost(
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val viewmodel = getKoin().get<SplashViewModel>()
    ObserveAsEvent(viewmodel.splashAction) {
        when (it) {
            is SplashViewModel.SplashAction.NavigateToHome -> navigateToHome()
            is SplashViewModel.SplashAction.NavigateToLogin -> navigateToLogin()
        }
    }

    SplashScreen()
}