package com.ranjan.somiq.profile.domain.usecase

import com.ranjan.somiq.profile.data.model.ProfileResponse
import com.ranjan.somiq.profile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(userId: String? = null): Result<ProfileResponse> = 
        profileRepository.getProfile(userId)
}
