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
) : BaseViewModel<ConversationContract.UiState, ConversationContract.Action, ConversationContract.Effect>() {

    override val initialState: ConversationContract.UiState
        get() = ConversationContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName)

    init {
        handleAction(ConversationContract.Action.LoadMessages)
    }

    override fun onAction(action: ConversationContract.Action) {
        viewModelScope.launch {
            when (action) {
                is ConversationContract.Action.LoadMessages -> loadMessages()
                is ConversationContract.Action.MessageTextChange -> setState { copy(messageText = action.text) }
                is ConversationContract.Action.SendMessage -> sendMessage()
                is ConversationContract.Action.ClearError -> setState { copy(error = null) }
                is ConversationContract.Action.Retry -> {
                    setState { copy(error = null) }
                    loadMessages()
                }
                is ConversationContract.Action.StartVoiceCall ->
                    emitEffect(ConversationContract.Effect.StartVoiceCall(otherUserId))
                is ConversationContract.Action.StartVideoCall ->
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
