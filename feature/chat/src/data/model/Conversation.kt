package com.ranjan.somiq.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    @SerialName("userId")
    val otherUserId: String,
    val otherUserName: String,
    val otherUserUsername: String? = null,
    val otherUserProfilePictureUrl: String? = null,
    val lastMessage: String? = null,
    val lastMessageTimestamp: Long = 0L,
    val unreadCount: Int = 0
)
