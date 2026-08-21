package com.ranjan.somiq.splash

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.presentation.util.collectEffects
import com.ranjan.somiq.splash.SplashContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreenHost(
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val viewModel: SplashViewModel = koinViewModel()
    viewModel.collectEffects {
        when (it) {
            is Effect.NavigateToHome -> navigateToHome()
            is Effect.NavigateToLogin -> navigateToLogin()
        }
    }

    SplashScreen()
}