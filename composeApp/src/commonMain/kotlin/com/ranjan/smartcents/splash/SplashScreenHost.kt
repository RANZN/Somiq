package com.ranjan.smartcents.splash

import androidx.compose.runtime.Composable
import com.ranjan.smartcents.util.ObserveAsEvent
import com.ranjan.smartcents.presentation.splash.SplashViewModel
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