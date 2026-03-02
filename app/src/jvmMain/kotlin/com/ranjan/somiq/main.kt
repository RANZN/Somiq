package com.ranjan.somiq

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ranjan.somiq.core.di.KotlinInitializer
import com.ranjan.somiq.di.getAllAppModules

fun main() = application {
    KotlinInitializer().init(
        additionalModules = getAllAppModules()
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "Somiq",
    ) {
        App()
    }
}