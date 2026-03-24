package com.ranjan.somiq.auth.ui.completeprofile

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.Effect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CompleteProfileScreenHost(
    signupToken: String,
    navigateHome: () -> Unit,
) {
    val viewModel: CompleteProfileViewModel = koinViewModel { parametersOf(signupToken) }
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) {
        when (it) {
            Effect.NavigateHome -> navigateHome()
        }
    }

    CompleteProfileScreen(
        uiState = uiState,
        modifier = Modifier.statusBarsPadding(),
        intent = viewModel::handleIntent,
    )
}
