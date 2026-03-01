package com.ranjan.somiq.chat.domain.usecase

import com.ranjan.somiq.chat.domain.repository.ChatRepository

class GetConversationsUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke() = chatRepository.getConversations()
}
