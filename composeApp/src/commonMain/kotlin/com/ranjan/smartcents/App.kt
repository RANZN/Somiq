package com.ranjan.smartcents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ranjan.smartcents.navigation.AppNavigation

@Composable
fun App() {
    MyApplicationTheme {
        Scaffold { innerPadding ->
            AppNavigation(Modifier.padding(innerPadding))
        }
    }
}