package com.ranjan.somiq.auth.ui.otp

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.auth.ui.otp.OtpContract.Effect
import com.ranjan.somiq.core.presentation.util.collectEffects
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun OtpScreenHost(
    phone: String,
    navigateHome: () -> Unit,
    navigateCompleteProfile: (signupToken: String) -> Unit,
    navigateBackToPhone: () -> Unit,
) {
    val viewModel: OtpViewModel = koinViewModel { parametersOf(phone) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects {
        when (it) {
            Effect.NavigateHome -> navigateHome()
            is Effect.NavigateCompleteProfile -> navigateCompleteProfile(it.signupToken)
            Effect.NavigateBackToPhone -> navigateBackToPhone()
        }
    }
    OtpScreen(
        uiState = uiState,
        modifier = Modifier.statusBarsPadding(),
        intent = viewModel::handleIntent,
    )
}
