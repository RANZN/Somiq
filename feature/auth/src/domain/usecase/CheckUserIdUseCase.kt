package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.repository.AuthRepository

class CheckUserIdUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(userId: String): Result<Boolean> =
        authRepository.checkUserIdAvailable(userId)
}
