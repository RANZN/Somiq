package com.ranjan.smartcents

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ranjan.smartcents.navigation.AppNavigation

@Composable
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}