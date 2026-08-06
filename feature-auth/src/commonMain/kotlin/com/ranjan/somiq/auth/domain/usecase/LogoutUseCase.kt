package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.repository.AuthRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean = withContext(NonCancellable) {
        authRepository.logoutUser()
    }
}
