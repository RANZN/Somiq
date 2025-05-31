package com.ranjan.smartcents.android.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.smartcents.android.util.ObserveAsEvent
import com.ranjan.smartcents.presentation.signup.SignupViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun SignupScreenHost(
    navigateToHome: () -> Unit
) {
    val viewmodel = remember { getKoin().get<SignupViewModel>() }

    ObserveAsEvent(viewmodel.navigateToHome) {
        navigateToHome()
    }

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    var error by remember { mutableStateOf("") }
    ObserveAsEvent(viewmodel.error) {
        error = it
    }
    SignupScreen(uiState, error) {
        when (it) {
            else -> viewmodel.handleAction(it)
        }
    }
}