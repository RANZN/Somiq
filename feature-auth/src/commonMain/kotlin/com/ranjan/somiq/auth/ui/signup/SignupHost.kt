package com.ranjan.somiq.auth.ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.auth.ui.signup.SignUpContract.Effect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignupScreenHost(
    navigateToHome: () -> Unit
) {
    val viewModel: SignupViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    CollectEffect(viewModel.effect) {
        when (it) {
            is Effect.NavigateToHome -> navigateToHome()
        }
    }

    SignupScreen(
        uiState = uiState,
        intent = viewModel::handleIntent,
    )
}