package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun EntryProviderScope<NavKey>.settingEntries(
    backStack: NavBackStack<NavKey>,
) {
    entry<Settings> {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    backStack.clear()
                    backStack.add(OnBoarding.Login)
                }
            ) {
                Text("Logout")
            }
        }
    }
}