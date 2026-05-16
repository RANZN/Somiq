package com.ranjan.somiq.chat.ui.conversation

import androidx.compose.runtime.Stable
import com.ranjan.somiq.chat.data.model.Message
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_load_messages
import com.ranjan.somiq.core.resources.error_failed_to_send_message

object ConversationContract {
    sealed class ScreenError : BaseScreenError {
        data object LoadMessagesFailed : ScreenError()
        data object SendMessageFailed : ScreenError()

        override fun toUiText(): UiText? = when (this) {
            LoadMessagesFailed -> UiText.Resource(Res.string.error_failed_to_load_messages)
            SendMessageFailed -> UiText.Resource(Res.string.error_failed_to_send_message)
        }
    }

    @Stable
    data class UiState(
        val otherUserId: String = "",
        val otherUserName: String = "",
        val messages: List<Message> = emptyList(),
        val messageText: String = "",
        val isLoading: Boolean = false,
        val sending: Boolean = false,
        val error: AppError? = null
    ) : BaseUiState {
        val hasError: Boolean get() = error != null
    }

    sealed interface Intent : BaseUiIntent {
        data object LoadMessages : Intent
        data class MessageTextChange(val text: String) : Intent
        data object SendMessage : Intent
        data object ClearError : Intent
        data object Retry : Intent
        data object StartVoiceCall : Intent
        data object StartVideoCall : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class ShowError(val message: AppError) : Effect
        data class StartVoiceCall(val userId: String) : Effect
        data class StartVideoCall(val userId: String) : Effect
    }
}
