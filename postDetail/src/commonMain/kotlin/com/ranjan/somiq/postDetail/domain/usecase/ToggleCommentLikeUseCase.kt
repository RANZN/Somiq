package com.ranjan.somiq.postDetail.domain.usecase

import com.ranjan.somiq.postDetail.domain.repository.CommentRepository

class ToggleCommentLikeUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: String): Result<Boolean> {
        return commentRepository.toggleLike(commentId)
    }
}
