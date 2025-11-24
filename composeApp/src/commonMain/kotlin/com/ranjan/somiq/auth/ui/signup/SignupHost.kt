package com.ranjan.somiq.auth.ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.auth.ui.signup.SignUpEvent.NavigateToHome
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignupScreenHost(
    viewmodel: SignupViewModel = koinViewModel(),
    navigateToHome: () -> Unit
) {

    ObserveAsEvent(viewmodel.events) {
        when (it) {
            is NavigateToHome -> navigateToHome()
        }
    }

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    SignupScreen(
        uiState = uiState,
        action = viewmodel::handleAction,
    )
}