package com.ranjan.somiq.createpost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePostScreenHost(
    onBack: () -> Unit,
    onRequestPickImage: () -> Unit,
    viewModel: CreatePostViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is CreatePostContract.Effect.PostSuccess -> onBack()
        }
    }

    CreatePostScreen(
        state = state,
        onCaptionChange = { viewModel.handleIntent(CreatePostContract.Intent.CaptionChange(it)) },
        onPickImageClick = onRequestPickImage,
        onPostClick = { viewModel.handleIntent(CreatePostContract.Intent.Post) },
        onBack = onBack
    )
}
