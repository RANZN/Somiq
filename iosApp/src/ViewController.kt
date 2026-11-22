package com.ranjan.somiq

import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import platform.UIKit.UIViewController

fun ViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}