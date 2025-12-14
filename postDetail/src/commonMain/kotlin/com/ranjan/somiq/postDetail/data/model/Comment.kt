package com.ranjan.somiq.postDetail.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    @SerialName("commentId")
    val id: String,
    val content: String,
    @SerialName("authorId")
    val authorId: String,
    val authorName: String,
    val authorUsername: String?,
    val authorProfilePictureUrl: String?,
    @SerialName("parentCommentId")
    val parentCommentId: String? = null,
    val createdAt: Long,
    val updatedAt: Long?,
    val likesCount: Long,
    val repliesCount: Long,
    val isLiked: Boolean = false
)

@Serializable
data class CreateCommentRequest(val content: String, val parentCommentId: String? = null)

@Serializable
data class UpdateCommentRequest(val content: String)
