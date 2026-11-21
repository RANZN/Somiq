package com.ranjan.somiq.auth.ui.phone

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.Effect
import com.ranjan.somiq.core.presentation.util.collectEffects
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PhoneEntryScreenHost(
    navigateToOtp: (phone: String) -> Unit,
) {
    val viewModel: PhoneEntryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects {
        when (it) {
            is Effect.NavigateToOtp -> navigateToOtp(it.phone)
        }
    }
    PhoneEntryScreen(
        uiState = uiState,
        modifier = Modifier.statusBarsPadding(),
        intent = viewModel::handleIntent,
    )
}
