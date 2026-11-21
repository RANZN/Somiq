package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.repository.AuthRepository

class CompleteSignupUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        signupToken: String,
        name: String,
        userId: String,
        email: String?,
        profilePictureUrl: String?,
    ): AuthResult = authRepository.completeSignup(
        signupToken = signupToken,
        name = name,
        userId = userId,
        email = email,
        profilePictureUrl = profilePictureUrl,
    )
}
