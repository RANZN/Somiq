package com.ranjan.somiq.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.ranjan.somiq.core.presentation.theme.Color1D2C5E
import com.ranjan.somiq.icons.SomiQSplashIcon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color1D2C5E
    ) {
        Image(
            imageVector = SomiQSplashIcon,
            contentDescription = "App Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}