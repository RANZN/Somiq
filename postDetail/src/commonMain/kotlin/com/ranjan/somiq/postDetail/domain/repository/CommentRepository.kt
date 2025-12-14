package com.ranjan.somiq.postDetail.domain.repository

import com.ranjan.somiq.postDetail.data.model.CommentResponse

interface CommentRepository {
    suspend fun getComments(
        postId: String? = null,
        reelId: String? = null,
        parentCommentId: String? = null,
        after: String? = null,
        limit: Int = 20
    ): Result<List<CommentResponse>>
    
    suspend fun createComment(
        postId: String? = null,
        reelId: String? = null,
        content: String,
        parentCommentId: String? = null
    ): Result<CommentResponse>
    
    suspend fun updateComment(commentId: String, content: String): Result<CommentResponse>
    
    suspend fun deleteComment(commentId: String): Result<Unit>
    
    suspend fun toggleLike(commentId: String): Result<Boolean>
}
