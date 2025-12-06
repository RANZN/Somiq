package com.ranjan.somiq

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.presentation.navigation.AppNavigation
import com.ranjan.somiq.core.presentation.theme.MyApplicationTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MyApplicationTheme {
        AppNavigation()
    }
}