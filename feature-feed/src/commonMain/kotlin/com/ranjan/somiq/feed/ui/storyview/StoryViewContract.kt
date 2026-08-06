package com.ranjan.somiq.feed.ui.storyview

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_load_story
import com.ranjan.somiq.feed.domain.model.Story

object StoryViewContract {

    sealed class ScreenError : BaseScreenError {
        data object LoadStoryFailed : ScreenError()

        override fun toUiText(): UiText = when (this) {
            LoadStoryFailed -> UiText.Resource(Res.string.error_failed_to_load_story)
        }
    }

    @Stable
    data class UiState(
        val story: Story? = null,
        val isLoading: Boolean = true,
        val error: AppError? = null
    ) : BaseUiState

    sealed interface Intent : BaseUiIntent

    sealed interface Effect : BaseUiEffect
}
