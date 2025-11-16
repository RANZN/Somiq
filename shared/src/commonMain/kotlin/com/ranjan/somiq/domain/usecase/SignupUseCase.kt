package com.ranjan.somiq.domain.usecase

import com.ranjan.somiq.domain.model.AuthResult
import com.ranjan.somiq.domain.repository.AuthRepository


class SignupUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(name: String, email: String, password: String): AuthResult {
        return authRepository.signUpUser(name, email, password)
    }

}