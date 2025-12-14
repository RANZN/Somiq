package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.data.model.ToggleResponse
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class ToggleLikeUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(postId: String): Result<ToggleResponse> {
        return repository.toggleLike(postId)
    }
}
