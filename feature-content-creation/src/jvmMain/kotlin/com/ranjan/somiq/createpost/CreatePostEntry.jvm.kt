package com.ranjan.somiq.createpost

import androidx.compose.runtime.Composable

@Composable
actual fun CreatePostEntry(onBack: () -> Unit) {
    CreatePostScreenHost(
        onBack = onBack,
        onRequestPickImage = { /* no-op on JVM */ }
    )
}
