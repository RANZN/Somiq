package com.ranjan.somiq.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val data: List<Post>,
    val nextCursor: String? = null
)

@Serializable
data class Post(
    @SerialName("postId")
    val id: String,
    val title: String,
    val content: String,
    @SerialName("authorId")
    val userId: String,
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
    val id: String,
    val userId: String,
    val username: String,
    val avatar: String? = null,
    val isViewed: Boolean = false
)

