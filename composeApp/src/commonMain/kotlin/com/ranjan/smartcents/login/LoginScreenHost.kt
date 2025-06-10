package com.ranjan.smartcents.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.ranjan.smartcents.presentation.login.LoginViewModel
import com.ranjan.smartcents.presentation.login.LoginViewModel.LoginAction
import com.ranjan.smartcents.util.ObserveAsEvent
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun LoginScreenHost(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val loginViewModel = remember { getKoin().get<LoginViewModel>() }
    val uiState by loginViewModel.uiState.collectAsState()
    ObserveAsEvent(loginViewModel.navigateToHome) {
        onLoginSuccess()
    }

    LoginScreen(
        uiState = uiState,
    ) { action ->
        when (action) {
            LoginAction.NavigateToSignUp -> onSignUpClick()
            else -> loginViewModel.handleAction(action)
        }
    }
}