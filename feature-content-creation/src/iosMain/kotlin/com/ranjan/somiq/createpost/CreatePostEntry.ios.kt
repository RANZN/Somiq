package com.ranjan.somiq.createpost

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun CreatePostEntry(onBack: () -> Unit) {
    CreatePostScreenHost(
        onBack = onBack,
        onRequestPickImage = { },
        viewModel = koinViewModel()
    )
}
