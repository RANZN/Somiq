package com.ranjan.somiq.chat.domain.usecase

import com.ranjan.somiq.chat.data.model.Message
import com.ranjan.somiq.chat.domain.repository.ChatRepository

class GetMessagesUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(userId: String, limit: Int = 50, before: String? = null): Result<List<Message>> =
        chatRepository.getMessages(userId, limit, before)
}
