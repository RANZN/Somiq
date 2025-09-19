package com.ranjan.smartcents.domain.usecase

import com.ranjan.smartcents.domain.model.AuthResult
import com.ranjan.smartcents.domain.repository.AuthRepository


class SignupUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(name: String, email: String, password: String): AuthResult {
        return authRepository.signUpUser(name, email, password)
    }

}