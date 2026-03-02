package com.ranjan.somiq.chat.ui.conversation

import androidx.compose.runtime.Stable
import com.ranjan.somiq.chat.data.model.Message
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object ConversationContract {
    @Stable
    data class UiState(
        val otherUserId: String = "",
        val otherUserName: String = "",
        val messages: List<Message> = emptyList(),
        val messageText: String = "",
        val isLoading: Boolean = false,
        val sending: Boolean = false,
        val error: String? = null
    ) : BaseUiState {
        val hasError: Boolean get() = error != null
    }

    sealed interface Action : BaseUiAction {
        data object LoadMessages : Action
        data class MessageTextChange(val text: String) : Action
        data object SendMessage : Action
        data object ClearError : Action
        data object Retry : Action
        data object StartVoiceCall : Action
        data object StartVideoCall : Action
    }

    sealed interface Effect : BaseUiEffect {
        data class ShowError(val message: String) : Effect
        data class StartVoiceCall(val userId: String) : Effect
        data class StartVideoCall(val userId: String) : Effect
    }
}
