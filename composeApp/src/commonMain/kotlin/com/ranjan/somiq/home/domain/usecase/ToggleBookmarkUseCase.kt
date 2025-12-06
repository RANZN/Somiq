package com.ranjan.somiq.home.domain.usecase

import com.ranjan.somiq.home.domain.repository.HomeRepository

class ToggleBookmarkUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(postId: String): Result<Boolean> {
        return repository.toggleBookmark(postId)
    }
}

