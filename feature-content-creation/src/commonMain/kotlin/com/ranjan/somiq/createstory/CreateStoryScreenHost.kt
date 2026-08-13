package com.ranjan.somiq.createstory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateStoryScreenHost(
    onBack: () -> Unit,
) {
    val viewModel: CreateStoryViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
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
