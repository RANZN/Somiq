package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.domain.repository.FeedRepository

class ToggleBookmarkUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(postId: String): Result<Boolean> {
        return repository.toggleBookmark(postId)
    }
}
