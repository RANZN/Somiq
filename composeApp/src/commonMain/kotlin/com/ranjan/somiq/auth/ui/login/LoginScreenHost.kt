package com.ranjan.somiq.auth.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.ranjan.somiq.auth.ui.login.LoginViewModel.LoginAction
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun LoginScreenHost(
    viewmodel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsState()
    ObserveAsEvent(viewmodel.navigateToHome) {
        onLoginSuccess()
    }

    LoginScreen(
        uiState = uiState,
    ) { action ->
        when (action) {
            LoginAction.NavigateToSignUp -> onSignUpClick()
            else -> viewmodel.handleAction(action)
        }
    }
}