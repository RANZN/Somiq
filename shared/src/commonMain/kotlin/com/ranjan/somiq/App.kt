package com.ranjan.somiq

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.presentation.snackbar.CollectGlobalUiEffects
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.navigation.AppNavigation
import com.ranjan.somiq.presentation.theme.MyApplicationTheme
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {

    val authStateManager = koinInject<AuthStateManager>()
    val userId = authStateManager.userId.collectAsStateWithLifecycle(null)

    MyApplicationTheme {
        key(userId) {
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
