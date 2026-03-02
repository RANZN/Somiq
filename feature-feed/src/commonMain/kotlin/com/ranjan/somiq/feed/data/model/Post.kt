package com.ranjan.somiq.feed.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryResponse(
    val data: List<Story>,
    val nextCursor: String? = null
)

@Serializable
data class Post(
    @SerialName("postId")
    val id: String,
    val title: String,
    val content: String,
    @SerialName("authorId")
    val authorId: String,
    val authorName: String,
    val authorUsername: String?,
    val authorProfilePictureUrl: String?,
    val createdAt: Long,
    val updatedAt: Long?,
    val mediaUrls: List<String>,
    val likesCount: Long,
    val bookmarksCount: Long,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false
)

@Serializable
data class Story(
    @SerialName("storyId")
    val id: String,
    val mediaUrl: String,
    val mediaType: MediaType,
    @SerialName("authorId")
    val authorId: String,
    val authorName: String,
    val authorUsername: String?,
    val authorProfilePictureUrl: String?,
    val createdAt: Long,
    val expiresAt: Long,
    val viewsCount: Long,
    val isViewed: Boolean = false
)

@Serializable
enum class MediaType {
    IMAGE,
    VIDEO
}
