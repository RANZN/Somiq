package com.ranjan.smartcents.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import smartcents.composeapp.generated.resources.Res
import smartcents.composeapp.generated.resources.splash_img

@Composable
fun SplashScreen() {
    Image(
        painter = painterResource(Res.drawable.splash_img),
        contentDescription = "splash screen",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}