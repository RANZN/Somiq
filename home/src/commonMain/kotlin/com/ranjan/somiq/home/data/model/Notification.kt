package com.ranjan.somiq.home.data.model

import com.ranjan.somiq.core.domain.model.TargetType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    @SerialName("notificationId")
    val id: String,
    val type: NotificationType,
    val message: String,
    @SerialName("actorId")
    val actorId: String,
    val actorName: String,
    val actorUsername: String?,
    val actorProfilePictureUrl: String?,
    val targetId: String?, // postId, reelId, commentId, etc.
    val targetType: TargetType?, // POST, REEL, COMMENT
    val createdAt: Long,
    val isRead: Boolean
)

@Serializable
enum class NotificationType {
    LIKE_POST,
    LIKE_REEL,
    LIKE_COMMENT,
    COMMENT_POST,
    COMMENT_REEL,
    REPLY_COMMENT,
    FOLLOW,
    MENTION
}

