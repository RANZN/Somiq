package com.ranjan.somiq.chat.ui.chatlist

import androidx.compose.runtime.Stable
import com.ranjan.somiq.chat.data.model.Conversation
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_load_chats
import com.ranjan.somiq.core.resources.error_failed_to_refresh_chats

object ChatListContract {

    sealed class ScreenError : BaseScreenError {
        data object LoadChatsFailed : ScreenError()
        data object RefreshChatsFailed : ScreenError()

        override fun toUiText(): UiText? {
            return when (this) {
                LoadChatsFailed -> UiText.Resource(Res.string.error_failed_to_load_chats)
                RefreshChatsFailed -> UiText.Resource(Res.string.error_failed_to_refresh_chats)
            }
        }
    }

    @Stable
    data class UiState(
        val conversations: List<Conversation> = emptyList(),
        val isLoading: Boolean = false,
        val error: AppError? = null,
        val refreshing: Boolean = false,
    ) : BaseUiState {
        val isEmpty: Boolean get() = conversations.isEmpty() && !isLoading && error == null
        val hasError: Boolean get() = error != null
    }

    sealed interface Intent : BaseUiIntent {
        data object LoadConversations : Intent
        data object Refresh : Intent
        data class OnConversationClick(val userId: String) : Intent
        data object ClearError : Intent
        data object Retry : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToConversation(val userId: String) : Effect
    }
}
