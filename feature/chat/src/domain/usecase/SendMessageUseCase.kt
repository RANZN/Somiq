package com.ranjan.somiq.chat.domain.usecase

import com.ranjan.somiq.chat.data.model.Message
import com.ranjan.somiq.chat.domain.repository.ChatRepository

class SendMessageUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(receiverId: String, content: String): Result<Message> =
        chatRepository.sendMessage(receiverId, content)
}
