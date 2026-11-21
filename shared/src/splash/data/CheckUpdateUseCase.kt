package com.ranjan.somiq.splash.data

class CheckUpdateUseCase(
    private val repository: CheckForUpdateRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isUpdateNeeded().getOrDefault(false)
    }

}