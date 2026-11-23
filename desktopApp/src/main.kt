package com.ranjan.somiq

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ranjan.somiq.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Somiq",
    ) {
        App()
    }
}
