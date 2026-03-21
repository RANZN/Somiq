package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.feed.domain.repository.FeedRepository

class GetStoriesUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(): Result<List<Story>> {
        return repository.getStories()
    }
}
