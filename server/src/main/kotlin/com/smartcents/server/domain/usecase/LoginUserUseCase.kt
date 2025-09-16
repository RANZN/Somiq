package com.smartcents.server.domain.usecase

import com.smartcents.server.domain.model.AuthResponse
import com.smartcents.server.domain.model.LoginRequest
import com.smartcents.server.domain.service.PasswordCipher
import com.smartcents.server.domain.service.TokenProvider
import com.smartcents.server.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
    private val passwordCipher: PasswordCipher,
) {

    suspend fun execute(loginRequest: LoginRequest): Result<AuthResponse> = runCatching {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw SecurityException("Invalid email or password")

        val isPasswordCorrect = passwordCipher.verifyPassword(loginRequest.password, user.hashedPassword)

        if (isPasswordCorrect) {
            val token = tokenProvider.createToken(user)
            AuthResponse(token, user.asResponse())
        } else {
            throw SecurityException("Invalid email or password")
        }
    }
}