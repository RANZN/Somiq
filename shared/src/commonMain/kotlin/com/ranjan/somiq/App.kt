package com.ranjan.somiq

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.app.AppViewModel
import com.ranjan.somiq.core.presentation.snackbar.CollectGlobalUiEffects
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.navigation.AppNavigation
import com.ranjan.somiq.presentation.theme.MyApplicationTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val appViewModel: AppViewModel = koinViewModel()
    val session by appViewModel.sessionState.collectAsStateWithLifecycle()

    MyApplicationTheme {
        key(session.key) {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(LocalSnackbar provides snackbarHostState) {
                CollectGlobalUiEffects()
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    content = {
                        AppNavigation()
                    },
                )
            }
        }
    }
}
