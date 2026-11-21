package com.ranjan.somiq

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ranjan.somiq.core.platform.JvmMediaPicker
import com.ranjan.somiq.core.platform.MediaPicker
import org.koin.dsl.module

fun main() = application {
    val coreModule = module {
        single<MediaPicker> { JvmMediaPicker() }
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Somiq",
    ) {
        App(koinConfig = {
            modules(coreModule)
        })
    }
}
