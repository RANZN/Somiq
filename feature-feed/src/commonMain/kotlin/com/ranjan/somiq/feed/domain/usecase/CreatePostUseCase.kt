package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.data.model.CreatePostRequest
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class CreatePostUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(request: CreatePostRequest): Result<Post> {
        return repository.createPost(request)
    }
}
