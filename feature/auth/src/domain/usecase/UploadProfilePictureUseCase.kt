package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.repository.AuthRepository

class UploadProfilePictureUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        signupToken: String,
        imageBytes: ByteArray,
        fileName: String
    ): Result<String> {
        return authRepository.uploadProfilePicture(signupToken, imageBytes, fileName)
    }
}
