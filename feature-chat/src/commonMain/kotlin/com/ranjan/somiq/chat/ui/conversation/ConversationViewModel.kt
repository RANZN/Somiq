package com.ranjan.somiq.chat.ui.conversation

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.chat.domain.usecase.GetMessagesUseCase
import com.ranjan.somiq.chat.domain.usecase.SendMessageUseCase
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val otherUserId: String,
    private val otherUserName: String,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel<ConversationContract.Intent, ConversationContract.Effect>() {

    private val _uiState = MutableStateFlow(ConversationContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName))
    val uiState = _uiState.asStateFlow()
    init {
        handleIntent(ConversationContract.Intent.LoadMessages)
    }
    override fun onIntent(intent: ConversationContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is ConversationContract.Intent.LoadMessages -> loadMessages()
                is ConversationContract.Intent.MessageTextChange -> _uiState.update {it.copy(messageText = intent.text) }
                is ConversationContract.Intent.SendMessage -> sendMessage()
                is ConversationContract.Intent.ClearError -> _uiState.update {it.copy(error = null) }
                is ConversationContract.Intent.Retry -> {
                    _uiState.update {it.copy(error = null) }
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
        _uiState.update {it.copy(isLoading = true, error = null) }
        getMessagesUseCase(otherUserId)
            .onSuccess { list -> _uiState.update {it.copy(messages = list, isLoading = false, error = null) } }
            .onFailure { e ->
                val appError = e.toAppError(ConversationContract.ScreenError.LoadMessagesFailed)
                _uiState.update {it.copy(isLoading = false, error = appError) }
                showSnackbar(appError)
            }
    }
    private suspend fun sendMessage() {
        val text = uiState.value.messageText.trim()
        if (text.isBlank()) return
        _uiState.update {it.copy(messageText = "", sending = true) }
        sendMessageUseCase(otherUserId, text)
            .onSuccess { message ->
                _uiState.update {it.copy(
                        messages = it.messages + message,
                        sending = false
                    )
                }
            }
            .onFailure { e ->
                _uiState.update {it.copy(messageText = text, sending = false) }
                showSnackbar(
                    e.toAppError(ConversationContract.ScreenError.SendMessageFailed),
                )
            }
    }
}
