package com.ranjan.somiq.auth.data.repository

import com.ranjan.somiq.auth.data.model.ProfileResponse
import com.ranjan.somiq.auth.domain.repository.ProfileRepository
import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val profilePictureUrl: String? = null
)

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

