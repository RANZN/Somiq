package com.ranjan.somiq.createpost

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.collectEffects
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePostScreenHost(
    onBack: () -> Unit,
) {
    val viewModel: CreatePostViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects { effect ->
        when (effect) {
            is CreatePostContract.Effect.PostSuccess -> onBack()
        }
    }
    CreatePostScreen(
        state = state,
        onCaptionChange = { viewModel.handleIntent(CreatePostContract.Intent.CaptionChange(it)) },
        onPickImageClick = {
            viewModel.handleIntent(CreatePostContract.Intent.PickImage)
        },
        onRemoveImageClick = { index ->
            viewModel.handleIntent(CreatePostContract.Intent.RemoveImage(index))
        },
        onPostClick = { viewModel.handleIntent(CreatePostContract.Intent.Post) },
        onBack = onBack
    )
}
