package com.ranjan.smartcents.android.splash

import com.ranjan.smartcents.android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun SplashScreen() {
    Image(
        painter = painterResource(R.drawable.splash_img),
        contentDescription = "splash screen",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}