package com.ranjan.somiq

import androidx.compose.ui.window.ComposeUIViewController
import com.ranjan.somiq.core.di.KotlinInitializer
import com.ranjan.somiq.di.getAllAppModules
import platform.UIKit.UIViewController

// Initialize Koin at module load time (when this file is first loaded)
private val koinInitialized = run {
    KotlinInitializer().init(
        additionalModules = getAllAppModules()
    )
}

fun MainViewController(): UIViewController {
    // Koin is already initialized at module load time
    return ComposeUIViewController { App() }
}