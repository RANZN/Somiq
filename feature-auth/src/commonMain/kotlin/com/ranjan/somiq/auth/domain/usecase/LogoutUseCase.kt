package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.core.data.local.AuthStateManager
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val authStateManager: AuthStateManager
) {
    suspend operator fun invoke(): Boolean = withContext(NonCancellable) {
        val response = authRepository.logoutUser()
        if (response) authStateManager.clearUserId()
        response
    }
}
