package com.smartcents.server.domain.usecase

import com.smartcents.server.domain.model.AuthResponse
import com.smartcents.server.domain.model.SignupRequest
import com.smartcents.server.domain.model.User
import com.smartcents.server.domain.service.PasswordCipher
import com.smartcents.server.domain.service.TokenProvider
import com.smartcents.server.domain.repository.UserRepository
import java.util.UUID

class SignUpUserUseCase(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
    private val passwordCipher: PasswordCipher,
) {

    suspend fun execute(signUpRequest: SignupRequest): Result<AuthResponse> = runCatching {
        if (!signUpRequest.email.contains("@") || signUpRequest.password.length < 8) {
            throw IllegalArgumentException("Invalid email or password must be at least 8 characters.")
        }

        if (userRepository.isEmailExists(signUpRequest.email)) {
            throw IllegalStateException("User already exists with this email")
        }

        val hashedPassword = passwordCipher.hashPassword(signUpRequest.password)

        val newUser = User(
            id = UUID.randomUUID(), // Generate the ID here
            name = signUpRequest.name,
            email = signUpRequest.email,
            hashedPassword = hashedPassword
        )

        val savedUser = userRepository.saveUser(newUser) ?: throw Exception("Failed to save user")

        val token = tokenProvider.createToken(savedUser)

        AuthResponse(token = token, user = savedUser.asResponse())
    }

}