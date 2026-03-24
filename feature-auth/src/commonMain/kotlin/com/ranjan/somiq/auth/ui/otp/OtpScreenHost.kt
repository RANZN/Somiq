package com.ranjan.somiq.auth.ui.otp

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.auth.ui.otp.OtpContract.Effect
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun OtpScreenHost(
    phone: String,
    navigateHome: () -> Unit,
    navigateCompleteProfile: (signupToken: String) -> Unit,
    navigateBackToPhone: () -> Unit,
) {
    val globalEffectDispatcher: GlobalEffectDispatcher = koinInject()
    val viewModel: OtpViewModel = koinViewModel { parametersOf(phone) }
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) {
        when (it) {
            Effect.NavigateHome -> navigateHome()
            is Effect.NavigateCompleteProfile -> navigateCompleteProfile(it.signupToken)
            Effect.NavigateBackToPhone -> navigateBackToPhone()
            is Effect.ShowSnackbar -> {
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(it.message, duration = it.duration)
                )
            }
        }
    }

    OtpScreen(
        uiState = uiState,
        modifier = Modifier.statusBarsPadding(),
        intent = viewModel::handleIntent,
    )
}
