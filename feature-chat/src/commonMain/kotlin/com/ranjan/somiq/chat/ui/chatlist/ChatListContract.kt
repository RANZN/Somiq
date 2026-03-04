package com.ranjan.somiq.chat.ui.chatlist

import androidx.compose.runtime.Stable
import com.ranjan.somiq.chat.data.model.Conversation
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object ChatListContract {
    @Stable
    data class UiState(
        val conversations: List<Conversation> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false,
        val showTopBar: Boolean = true
    ) : BaseUiState {
        val isEmpty: Boolean get() = conversations.isEmpty() && !isLoading && error == null
        val hasError: Boolean get() = error != null
    }

    sealed interface Intent : BaseUiIntent {
        data object LoadConversations : Intent
        data object Refresh : Intent
        data class SetShowTopBar(val show: Boolean) : Intent
        data class OnConversationClick(val userId: String) : Intent
        data object ClearError : Intent
        data object Retry : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToConversation(val userId: String) : Effect
    }
}
