package com.ranjan.somiq

import androidx.compose.ui.window.ComposeUIViewController
import com.ranjan.somiq.core.di.KotlinInitializer
import com.ranjan.somiq.di.getAllAppModules
import platform.UIKit.UIViewController

private val koinInitialized = run {
    KotlinInitializer().init(
        additionalModules = getAllAppModules()
    )
}

fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}
