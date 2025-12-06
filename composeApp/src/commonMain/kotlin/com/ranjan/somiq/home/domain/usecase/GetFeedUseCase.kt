package com.ranjan.somiq.home.domain.usecase

import com.ranjan.somiq.home.data.model.Post
import com.ranjan.somiq.home.domain.repository.HomeRepository

class GetFeedUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Post>> {
        return repository.getFeed()
    }
}

