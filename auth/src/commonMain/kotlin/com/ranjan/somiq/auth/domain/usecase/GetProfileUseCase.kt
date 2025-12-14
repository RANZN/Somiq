package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.data.model.ProfileResponse
import com.ranjan.somiq.auth.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(userId: String? = null): Result<ProfileResponse> = 
        profileRepository.getProfile(userId)
}

