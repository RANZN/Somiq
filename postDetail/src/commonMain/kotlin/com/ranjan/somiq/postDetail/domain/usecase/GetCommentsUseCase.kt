package com.ranjan.somiq.postDetail.domain.usecase

import com.ranjan.somiq.postDetail.data.model.CommentResponse
import com.ranjan.somiq.postDetail.domain.repository.CommentRepository

class GetCommentsUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        postId: String? = null,
        reelId: String? = null,
        parentCommentId: String? = null,
        after: String? = null,
        limit: Int = 20
    ): Result<List<CommentResponse>> {
        return commentRepository.getComments(postId, reelId, parentCommentId, after, limit)
    }
}
