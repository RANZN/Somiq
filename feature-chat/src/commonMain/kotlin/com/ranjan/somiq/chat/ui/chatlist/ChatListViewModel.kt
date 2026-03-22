package com.ranjan.somiq.chat.ui.chatlist

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.chat.domain.usecase.GetConversationsUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val getConversationsUseCase: GetConversationsUseCase
) : BaseViewModel<ChatListContract.UiState, ChatListContract.Intent, ChatListContract.Effect>() {

    override val initialState: ChatListContract.UiState
        get() = ChatListContract.UiState()

    init {
        handleIntent(ChatListContract.Intent.LoadConversations)
    }

    override fun onIntent(intent: ChatListContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is ChatListContract.Intent.LoadConversations -> loadConversations()
                is ChatListContract.Intent.Refresh -> refresh()
                is ChatListContract.Intent.OnConversationClick ->
                    emitEffect(ChatListContract.Effect.NavigateToConversation(intent.userId))
                is ChatListContract.Intent.ClearError -> setState { copy(error = null) }
                is ChatListContract.Intent.Retry -> {
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
