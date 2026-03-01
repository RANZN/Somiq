package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean = authRepository.logoutUser()
}
