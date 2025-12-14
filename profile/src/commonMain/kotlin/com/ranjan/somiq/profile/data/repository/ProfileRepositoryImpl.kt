package com.ranjan.somiq.profile.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.profile.data.model.ProfileResponse
import com.ranjan.somiq.profile.data.model.UpdateProfileRequest
import com.ranjan.somiq.profile.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class ProfileRepositoryImpl(
    private val httpClient: HttpClient
) : ProfileRepository {

    override suspend fun getProfile(userId: String?): Result<ProfileResponse> {
        val url = if (userId != null) {
            "$BASE_URL/v1/account/$userId"
        } else {
            "$BASE_URL/v1/account"
        }
        return safeApiCall(
            apiCall = { httpClient.get(url) },
            errorMessage = "Failed to load profile"
        )
    }

    override suspend fun updateProfile(
        name: String?,
        username: String?,
        bio: String?,
        profilePictureUrl: String?
    ): Result<ProfileResponse> {
        return safeApiCall(
            apiCall = {
                httpClient.put("$BASE_URL/v1/account/profile") {
                    setBody(
                        UpdateProfileRequest(
                            name = name,
                            username = username,
                            bio = bio,
                            profilePictureUrl = profilePictureUrl
                        )
                    )
                }
            },
            errorMessage = "Failed to update profile"
        )
    }
}
