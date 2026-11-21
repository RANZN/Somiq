package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

import kotlinx.coroutines.flow.StateFlow

class GetBookmarkedPostsUseCase(
    private val repository: FeedRepository
) {
    val bookmarkedPostsFlow: StateFlow<List<Post>> = repository.bookmarkedPostsFlow

    suspend operator fun invoke(): Result<List<Post>> {
        return repository.getBookmarkedPosts()
    }
}
