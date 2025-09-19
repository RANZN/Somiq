package com.ranjan.smartcents.domain.usecase

import com.ranjan.smartcents.domain.model.AuthResult
import com.ranjan.smartcents.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.loginUser(email, password)
    }

}