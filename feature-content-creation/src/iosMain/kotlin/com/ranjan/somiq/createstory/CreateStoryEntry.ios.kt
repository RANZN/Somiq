package com.ranjan.somiq.createstory

import androidx.compose.runtime.Composable

@Composable
actual fun CreateStoryEntry(onBack: () -> Unit) {
    CreateStoryScreenHost(
        onBack = onBack,
        onRequestPickImage = { /* no-op on iOS */ }
    )
}
