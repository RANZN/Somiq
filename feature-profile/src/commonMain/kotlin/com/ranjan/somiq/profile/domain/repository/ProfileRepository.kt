package com.ranjan.somiq.profile.domain.repository

import com.ranjan.somiq.profile.data.model.ProfileResponse

interface ProfileRepository {
    suspend fun getProfile(userId: String? = null): Result<ProfileResponse>
    suspend fun updateProfile(name: String?, username: String?, bio: String?, profilePictureUrl: String?): Result<ProfileResponse>
}
