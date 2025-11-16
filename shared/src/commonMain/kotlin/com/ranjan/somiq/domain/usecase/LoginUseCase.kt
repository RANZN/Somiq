package com.ranjan.somiq.domain.usecase

import com.ranjan.somiq.domain.model.AuthResult
import com.ranjan.somiq.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.loginUser(email, password)
    }

}