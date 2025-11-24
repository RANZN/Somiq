package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.repository.AuthRepository


class SignupUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(name: String, email: String, password: String): AuthResult {
        return authRepository.signUpUser(name, email, password)
    }

}