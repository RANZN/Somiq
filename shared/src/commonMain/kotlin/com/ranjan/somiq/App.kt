package com.ranjan.somiq

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.navigation.AppNavigation
import com.ranjan.somiq.presentation.theme.MyApplicationTheme

@Composable
@Preview
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }

    MyApplicationTheme {
        CompositionLocalProvider(LocalSnackbar provides snackbarHostState) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                content = {
                    AppNavigation()
                }
            )
        }
    }
}
