package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class GetFeedUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(): Result<List<Post>> {
        return repository.getFeed()
    }
}
