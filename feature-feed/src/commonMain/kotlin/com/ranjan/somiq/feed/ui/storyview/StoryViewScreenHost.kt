package com.ranjan.somiq.feed.ui.storyview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StoryViewScreenHost(
    storyId: String,
    onBack: () -> Unit
) {
    val viewModel: StoryViewViewModel = koinViewModel(
        parameters = { parametersOf(storyId) }
    )
    val state by viewModel.state.collectAsState()

    StoryViewScreen(
        state = state,
        onBack = onBack
    )
}
