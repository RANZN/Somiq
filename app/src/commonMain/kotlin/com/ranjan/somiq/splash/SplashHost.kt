package com.ranjan.somiq.splash

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.splash.SplashContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreenHost(
    viewmodel: SplashViewModel = koinViewModel(),
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    CollectEffect(viewmodel.effect) {
        when (it) {
            is Effect.NavigateToHome -> navigateToHome()
            is Effect.NavigateToLogin -> navigateToLogin()
        }
    }

    SplashScreen()
}