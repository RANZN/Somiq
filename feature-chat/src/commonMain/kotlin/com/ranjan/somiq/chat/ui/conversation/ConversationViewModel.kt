package com.ranjan.somiq.chat.ui.conversation

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.chat.domain.usecase.GetMessagesUseCase
import com.ranjan.somiq.chat.domain.usecase.SendMessageUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val otherUserId: String,
    private val otherUserName: String,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel<ConversationContract.UiState, ConversationContract.Intent, ConversationContract.Effect>(
    ConversationContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName)
) {

    init {
        handleIntent(ConversationContract.Intent.LoadMessages)
    }

    override fun onIntent(intent: ConversationContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is ConversationContract.Intent.LoadMessages -> loadMessages()
                is ConversationContract.Intent.MessageTextChange -> setState { copy(messageText = intent.text) }
                is ConversationContract.Intent.SendMessage -> sendMessage()
                is ConversationContract.Intent.ClearError -> setState { copy(error = null) }
                is ConversationContract.Intent.Retry -> {
                    setState { copy(error = null) }
                    loadMessages()
                }
                is ConversationContract.Intent.StartVoiceCall ->
                    emitEffect(ConversationContract.Effect.StartVoiceCall(otherUserId))
                is ConversationContract.Intent.StartVideoCall ->
                    emitEffect(ConversationContract.Effect.StartVideoCall(otherUserId))
            }
        }
    }

    private suspend fun loadMessages() {
        setState { copy(isLoading = true, error = null) }
        getMessagesUseCase(otherUserId)
            .onSuccess { list -> setState { copy(messages = list, isLoading = false, error = null) } }
            .onFailure { e -> setState { copy(isLoading = false, error = e.message ?: "Failed to load messages") } }
    }

    private suspend fun sendMessage() {
        val text = state.value.messageText.trim()
        if (text.isBlank()) return
        setState { copy(messageText = "", sending = true) }
        sendMessageUseCase(otherUserId, text)
            .onSuccess { message ->
                setState {
                    copy(
                        messages = messages + message,
                        sending = false
                    )
                }
            }
            .onFailure { e ->
                setState { copy(messageText = text, sending = false) }
                emitEffect(ConversationContract.Effect.ShowError(e.message ?: "Failed to send"))
            }
    }
}
