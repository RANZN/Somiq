package com.ranjan.smartcents.domain.usecase

import com.ranjan.smartcents.domain.repository.AppRepository

class CheckUpdate(
    private val repository: AppRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isUpdateNeeded().getOrDefault(false)
    }

}
