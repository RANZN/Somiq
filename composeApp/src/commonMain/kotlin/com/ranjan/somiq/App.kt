package com.ranjan.somiq

import androidx.compose.runtime.Composable
import com.ranjan.somiq.core.di.InitializeCoil
import com.ranjan.somiq.core.presentation.theme.MyApplicationTheme
import com.ranjan.somiq.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    InitializeCoil()
    
    MyApplicationTheme {
        AppNavigation()
    }
}