package com.ranjan.somiq.createstory

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun CreateStoryEntry(onBack: () -> Unit) {
    CreateStoryScreenHost(
        onBack = onBack,
        onRequestPickImage = { },
        viewModel = koinViewModel()
    )
}
