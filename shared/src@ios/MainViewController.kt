package com.ranjan.somiq

import androidx.compose.ui.window.ComposeUIViewController
import com.ranjan.somiq.core.platform.IosMediaPicker
import com.ranjan.somiq.core.platform.MediaPicker
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    lateinit var controller: UIViewController
    val coreModule = module {
        single<MediaPicker> {
            IosMediaPicker(controller) // creating this with controller without controller is assigned is completely fine. As koin resolves it when it is initialized.
        }
    }
    controller = ComposeUIViewController {
        App(
            koinConfig = { modules(coreModule) }
        )
    }
    return controller
}
