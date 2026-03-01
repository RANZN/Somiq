package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.loginUser(email, password)
    }

}