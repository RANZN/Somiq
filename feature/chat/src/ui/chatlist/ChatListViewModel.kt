package com.ranjan.somiq.chat.ui.chatlist

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.chat.domain.usecase.GetConversationsUseCase
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val getConversationsUseCase: GetConversationsUseCase
) : BaseViewModel<ChatListContract.Intent, ChatListContract.Effect>() {

    private val _uiState = MutableStateFlow(ChatListContract.UiState())
    val uiState = _uiState.asStateFlow()
    init {
        handleIntent(ChatListContract.Intent.LoadConversations)
    }
    override fun onIntent(intent: ChatListContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is ChatListContract.Intent.LoadConversations -> loadConversations()
                is ChatListContract.Intent.Refresh -> refresh()
                is ChatListContract.Intent.OnConversationClick -> {
                    emitEffect(ChatListContract.Effect.NavigateToConversation(intent.userId))
                }
                is ChatListContract.Intent.ClearError -> {
                    _uiState.update {it.copy(error = null) }
                }
                is ChatListContract.Intent.Retry -> {
                    _uiState.update {it.copy(error = null) }
                    loadConversations()
                }
            }
        }
    }
    private suspend fun loadConversations() {
        _uiState.update {it.copy(isLoading = true, error = null) }
        getConversationsUseCase()
            .onSuccess { list ->
                _uiState.update {it.copy(
                        conversations = list,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .onFailure { e ->
                _uiState.update {it.copy(
                        isLoading = false,
                        error = e.toAppError(ChatListContract.ScreenError.LoadChatsFailed)
                    )
                }
            }
    }
    private suspend fun refresh() {
        _uiState.update {it.copy(refreshing = true, error = null) }
        getConversationsUseCase()
            .onSuccess { list ->
                _uiState.update {it.copy(
                        conversations = list,
                        refreshing = false,
                        error = null
                    )
                }
            }
            .onFailure { e ->
                val appError = e.toAppError(ChatListContract.ScreenError.RefreshChatsFailed)
                _uiState.update {it.copy(refreshing = false, error = appError) }
                showSnackbar(appError)
            }
    }
}
