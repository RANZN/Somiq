package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class GetFeedPageUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(after: String? = null, limit: Int = 20): Result<PaginationResult<Post>> {
        return repository.getFeedPage(after, limit)
    }
}
