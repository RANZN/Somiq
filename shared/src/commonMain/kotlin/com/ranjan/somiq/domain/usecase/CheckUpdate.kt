package com.ranjan.somiq.domain.usecase

import com.ranjan.somiq.domain.repository.AppRepository

class CheckUpdate(
    private val repository: AppRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isUpdateNeeded().getOrDefault(false)
    }

}
