package com.ranjan.somiq.chat.data.repository

import com.ranjan.somiq.chat.data.model.Conversation
import com.ranjan.somiq.chat.data.model.Message
import com.ranjan.somiq.chat.data.model.SendMessageRequest
import com.ranjan.somiq.chat.domain.repository.ChatRepository
import io.ktor.client.HttpClient

class ChatRepositoryImpl(
    private val httpClient: HttpClient
) : ChatRepository {

    override suspend fun getConversations(): Result<List<Conversation>> = runCatching {
        // TODO: Replace with real API when backend is ready
        // httpClient.get("/api/chat/conversations").body<List<Conversation>>()
        emptyList<Conversation>()
    }

    override suspend fun getMessages(userId: String, limit: Int, before: String?): Result<List<Message>> = runCatching {
        // TODO: Replace with real API
        // httpClient.get("/api/chat/conversations/$userId/messages") { ... }.body()
        emptyList<Message>()
    }

    override suspend fun sendMessage(receiverId: String, content: String): Result<Message> = runCatching {
        // TODO: Replace with real API
        // httpClient.post("/api/chat/messages") { setBody(SendMessageRequest(content, receiverId)) }.body()
        Message(
            id = "mock-${System.currentTimeMillis()}",
            content = content,
            senderId = "current-user",
            receiverId = receiverId,
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )
    }
}
