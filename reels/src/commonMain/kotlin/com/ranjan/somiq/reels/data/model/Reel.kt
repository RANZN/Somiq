package com.ranjan.somiq.reels.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReelResponse(
    val data: List<Reel>,
    val nextCursor: String? = null
)

@Serializable
data class Reel(
    @SerialName("reelId")
    val id: String,
    val title: String,
    val description: String?,
    val videoUrl: String,
    val thumbnailUrl: String?,
    @SerialName("authorId")
    val authorId: String,
    val authorName: String,
    val authorUsername: String?,
    val authorProfilePictureUrl: String?,
    val createdAt: Long,
    val updatedAt: Long?,
    val likesCount: Long,
    val commentsCount: Long,
    val viewsCount: Long,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false
)
