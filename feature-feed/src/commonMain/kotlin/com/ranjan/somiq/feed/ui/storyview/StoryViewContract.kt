package com.ranjan.somiq.feed.ui.storyview

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.feed.data.model.Story

object StoryViewContract {
    @Stable
    data class UiState(
        val story: Story? = null,
        val isLoading: Boolean = true,
        val error: String? = null
    ) : BaseUiState

    sealed interface Intent : BaseUiIntent

    sealed interface Effect : BaseUiEffect
}
