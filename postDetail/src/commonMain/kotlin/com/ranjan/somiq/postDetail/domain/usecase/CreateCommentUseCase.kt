package com.ranjan.somiq.postDetail.domain.usecase

import com.ranjan.somiq.postDetail.data.model.CommentResponse
import com.ranjan.somiq.postDetail.domain.repository.CommentRepository

class CreateCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        postId: String? = null,
        reelId: String? = null,
        content: String,
        parentCommentId: String? = null
    ): Result<CommentResponse> {
        return commentRepository.createComment(postId, reelId, content, parentCommentId)
    }
}
