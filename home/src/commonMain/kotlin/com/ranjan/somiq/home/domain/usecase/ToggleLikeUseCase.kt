package com.ranjan.somiq.home.domain.usecase

import com.ranjan.somiq.home.domain.repository.HomeRepository

class ToggleLikeUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(postId: String): Result<Boolean> {
        return repository.toggleLike(postId)
    }
}

