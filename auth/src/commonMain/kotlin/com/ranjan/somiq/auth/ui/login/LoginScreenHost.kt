package com.ranjan.somiq.auth.ui.login

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenHost(
    viewmodel: LoginViewModel = koinViewModel(),
    navigateToDashboard: () -> Unit,
    navigateToSignUp: () -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsState()

    ObserveAsEvent(viewmodel.events) {
        when (it) {
            LoginEvent.NavigateToDashboard -> navigateToDashboard()
            LoginEvent.NavigateToSignUp -> navigateToSignUp()
        }
    }

    LoginScreen(
        uiState = uiState,
        modifier = Modifier.statusBarsPadding(),
        action = viewmodel::handleAction
    )
}