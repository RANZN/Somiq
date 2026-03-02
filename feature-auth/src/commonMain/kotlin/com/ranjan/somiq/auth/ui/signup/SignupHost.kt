package com.ranjan.somiq.auth.ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.auth.ui.signup.SignUpContract.Effect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignupScreenHost(
    viewmodel: SignupViewModel = koinViewModel(),
    navigateToHome: () -> Unit
) {

    CollectEffect(viewmodel.effect) {
        when (it) {
            is Effect.NavigateToHome -> navigateToHome()
        }
    }

    val uiState by viewmodel.state.collectAsStateWithLifecycle()

    SignupScreen(
        uiState = uiState,
        action = viewmodel::handleAction,
    )
}