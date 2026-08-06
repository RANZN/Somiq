package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class GetPostUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(postId: String): Result<Post> {
        return repository.getPost(postId)
    }
}
