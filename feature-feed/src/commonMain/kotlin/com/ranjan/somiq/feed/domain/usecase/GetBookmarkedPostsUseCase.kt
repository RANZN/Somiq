package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class GetBookmarkedPostsUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(): Result<List<Post>> {
        return repository.getBookmarkedPosts()
    }
}
