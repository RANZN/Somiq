package com.ranjan.somiq.chat.ui.chatlist

import androidx.compose.runtime.Stable
import com.ranjan.somiq.chat.data.model.Conversation
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object ChatListContract {
    @Stable
    data class UiState(
        val conversations: List<Conversation> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false
    ) : BaseUiState {
        val isEmpty: Boolean get() = conversations.isEmpty() && !isLoading && error == null
        val hasError: Boolean get() = error != null
    }

    sealed interface Action : BaseUiAction {
        data object LoadConversations : Action
        data object Refresh : Action
        data class OnConversationClick(val userId: String) : Action
        data object ClearError : Action
        data object Retry : Action
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToConversation(val userId: String) : Effect
    }
}
