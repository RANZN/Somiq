package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.ranjan.somiq.auth.domain.usecase.LogoutUseCase
import com.ranjan.somiq.navigation.AppNavGraph.OnBoarding
import com.ranjan.somiq.navigation.AppNavGraph.Settings
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

fun EntryProviderScope<NavKey>.settingEntries(backStack: NavBackStack<NavKey>) {
    entry<Settings> {
        val logoutUseCase = koinInject<LogoutUseCase>()
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        logoutUseCase.invoke()
                        backStack.clear()
                        backStack.add(OnBoarding.Login)
                    }
                }
            ) {
                Text("Logout")
            }
        }
    }
}