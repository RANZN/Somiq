package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class GetPostsByUserUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Post>> {
        return repository.getPostsByUser(userId)
    }
}
