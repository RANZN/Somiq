package com.ranjan.somiq.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("messageId")
    val id: String,
    val content: String,
    @SerialName("senderId")
    val senderId: String,
    @SerialName("receiverId")
    val receiverId: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    @SerialName("isFromMe")
    val isFromMe: Boolean = false
)

@Serializable
data class SendMessageRequest(
    val content: String,
    @SerialName("receiverId")
    val receiverId: String
)
