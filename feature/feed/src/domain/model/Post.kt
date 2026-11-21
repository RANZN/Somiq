package com.ranjan.somiq.feed.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Post(
    val id: String,
    val caption: String,
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

@Stable
data class Story(
    val id: String,
    val mediaUrl: String,
    val mediaType: MediaType,
    val authorId: String,
    val authorName: String,
    val authorUsername: String?,
    val authorProfilePictureUrl: String?,
    val createdAt: Long,
    val expiresAt: Long,
    val viewsCount: Long,
    val isViewed: Boolean = false
)

enum class MediaType {
    IMAGE,
    VIDEO
}

data class CreateStoryRequest(
    val mediaUrl: String,
    val mediaType: MediaType
)
