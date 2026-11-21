package com.ranjan.somiq.feed.domain.usecase

import com.ranjan.somiq.feed.domain.model.Story
import com.ranjan.somiq.feed.domain.repository.StoryRepository

class GetStoriesUseCase(
    private val repository: StoryRepository
) {
    suspend operator fun invoke(): Result<List<Story>> {
        return repository.getStories()
    }
}
