package com.ranjan.somiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ranjan.somiq.core.platform.AndroidMediaPicker
import com.ranjan.somiq.core.platform.MediaPicker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val mediaPicker = AndroidMediaPicker(this)
        val coreModules = module {
            single<MediaPicker> { mediaPicker }
        }

        setContent {
            App {
                androidContext(this@MainActivity)
                androidLogger()
                modules(coreModules)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
