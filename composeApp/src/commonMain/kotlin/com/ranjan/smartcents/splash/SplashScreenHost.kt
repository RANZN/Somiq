package com.ranjan.smartcents.android.splash

import androidx.compose.runtime.Composable
import com.ranjan.smartcents.android.util.ObserveAsEvent
import com.ranjan.smartcents.presentation.splash.SplashViewModel
import org.koin.compose.getKoin

@Composable
fun SplashScreenHost(
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val viewmodel = getKoin().get<SplashViewModel>()
    ObserveAsEvent(viewmodel.splashAction) {
        when (it) {
            SplashViewModel.SplashAction.NavigateToHome -> navigateToHome()
            SplashViewModel.SplashAction.NavigateToLogin -> navigateToLogin()
        }
    }

    SplashScreen()
}