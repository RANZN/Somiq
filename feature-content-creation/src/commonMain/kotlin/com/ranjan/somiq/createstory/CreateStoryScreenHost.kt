package com.ranjan.somiq.createstory

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.collectEffects
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateStoryScreenHost(
    onBack: () -> Unit,
) {
    val viewModel: CreateStoryViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects { effect ->
        when (effect) {
            is CreateStoryContract.Effect.StorySuccess -> onBack()
        }
    }
    CreateStoryScreen(
        state = state,
        onPickImageClick = {
            viewModel.handleIntent(CreateStoryContract.Intent.PickImage)
        },
        onPostClick = { viewModel.handleIntent(CreateStoryContract.Intent.Post) },
        onBack = onBack
    )
}
