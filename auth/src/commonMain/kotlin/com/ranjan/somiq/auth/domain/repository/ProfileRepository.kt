package com.ranjan.somiq.auth.domain.repository

import com.ranjan.somiq.auth.data.model.ProfileResponse

interface ProfileRepository {
    suspend fun getProfile(userId: String? = null): Result<ProfileResponse>
    suspend fun updateProfile(name: String?, username: String?, bio: String?, profilePictureUrl: String?): Result<ProfileResponse>
}

