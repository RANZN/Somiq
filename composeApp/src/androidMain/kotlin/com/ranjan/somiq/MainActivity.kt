package com.ranjan.somiq

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(SystemBarStyle.auto(Color.YELLOW, Color.YELLOW))
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.YELLOW, Color.YELLOW),
            navigationBarStyle = SystemBarStyle.light(Color.YELLOW, Color.YELLOW)
        )
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}