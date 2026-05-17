package com.ranjan.somiq.auth.ui.phone

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.Effect
import com.ranjan.somiq.core.presentation.effect.ShowSnackbarEffect
import com.ranjan.somiq.core.presentation.model.resolve
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PhoneEntryScreenHost(
    navigateToOtp: (phone: String) -> Unit,
) {
    val snackbar = LocalSnackbar.current
    val viewModel: PhoneEntryViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) {
        when (it) {
            is Effect.NavigateToOtp -> navigateToOtp(it.phone)
            is ShowSnackbarEffect -> snackbar.showSnackbar(it.message.resolve())
        }
    }

    PhoneEntryScreen(
        uiState = uiState,
        modifier = Modifier.statusBarsPadding(),
        intent = viewModel::handleIntent,
    )
}
