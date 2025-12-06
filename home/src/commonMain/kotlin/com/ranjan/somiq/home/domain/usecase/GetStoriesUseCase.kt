package com.ranjan.somiq.home.domain.usecase

import com.ranjan.somiq.home.data.model.Story
import com.ranjan.somiq.home.domain.repository.HomeRepository

class GetStoriesUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Story>> {
        return repository.getStories()
    }
}

