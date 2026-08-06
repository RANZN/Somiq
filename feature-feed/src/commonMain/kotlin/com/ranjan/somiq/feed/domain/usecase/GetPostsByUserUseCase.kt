package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

import kotlinx.coroutines.flow.StateFlow

class GetPostsByUserUseCase(
    private val repository: FeedRepository
) {
    val myPostsFlow: StateFlow<List<Post>> = repository.myPostsFlow

    suspend operator fun invoke(userId: String): Result<List<Post>> {
        return repository.getPostsByUser(userId)
    }
}
