package com.ranjan.somiq.chat.ui.chatlist

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.chat.domain.usecase.GetConversationsUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val getConversationsUseCase: GetConversationsUseCase
) : BaseViewModel<ChatListContract.UiState, ChatListContract.Action, ChatListContract.Effect>() {

    override val initialState: ChatListContract.UiState
        get() = ChatListContract.UiState()

    init {
        handleAction(ChatListContract.Action.LoadConversations)
    }

    override fun onAction(action: ChatListContract.Action) {
        viewModelScope.launch {
            when (action) {
                is ChatListContract.Action.LoadConversations -> loadConversations()
                is ChatListContract.Action.Refresh -> refresh()
                is ChatListContract.Action.OnConversationClick ->
                    emitEffect(ChatListContract.Effect.NavigateToConversation(action.userId))
                is ChatListContract.Action.ClearError -> setState { copy(error = null) }
                is ChatListContract.Action.Retry -> {
                    setState { copy(error = null) }
                    loadConversations()
                }
            }
        }
    }

    private suspend fun loadConversations() {
        setState { copy(isLoading = true, error = null) }
        getConversationsUseCase()
            .onSuccess { list -> setState { copy(conversations = list, isLoading = false, error = null) } }
            .onFailure { e -> setState { copy(isLoading = false, error = e.message ?: "Failed to load chats") } }
    }

    private suspend fun refresh() {
        setState { copy(refreshing = true, error = null) }
        getConversationsUseCase()
            .onSuccess { list -> setState { copy(conversations = list, refreshing = false, error = null) } }
            .onFailure { e -> setState { copy(refreshing = false, error = e.message ?: "Failed to refresh") } }
    }
}
