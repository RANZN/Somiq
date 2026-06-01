package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ranjan.somiq.navigation.AppNavGraph.*
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val settingNavigationModule = module {
    navigation<Settings> {
        val backStack = LocalNavBackStack.current
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