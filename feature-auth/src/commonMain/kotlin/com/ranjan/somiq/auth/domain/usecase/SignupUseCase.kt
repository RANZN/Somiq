package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.repository.AuthRepository


class SignupUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(name: String, username: String, email: String, password: String): AuthResult {
        // signUpUser now saves tokens directly, so no need to call loginUser
        return authRepository.signUpUser(name, username, email, password)
    }

}