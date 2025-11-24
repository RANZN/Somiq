package com.ranjan.somiq.common.checkForUpdate

class CheckUpdateUseCase(
    private val repository: CheckForUpdateRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isUpdateNeeded().getOrDefault(false)
    }

}