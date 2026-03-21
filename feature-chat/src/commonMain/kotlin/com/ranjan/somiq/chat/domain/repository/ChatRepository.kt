package com.ranjan.somiq.chat.domain.repository

import com.ranjan.somiq.chat.data.model.Conversation
import com.ranjan.somiq.chat.data.model.Message

interface ChatRepository {
    suspend fun getConversations(): Result<List<Conversation>>
    suspend fun getMessages(userId: String, limit: Int = 50, before: String? = null): Result<List<Message>>
    suspend fun sendMessage(receiverId: String, content: String): Result<Message>
}
