package com.ranjan.somiq.profile.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.profile.data.mapper.toDomain
import com.ranjan.somiq.profile.data.model.ProfileResponseDto
import com.ranjan.somiq.profile.data.model.UpdateProfileRequestDto
import com.ranjan.somiq.profile.domain.model.ProfileResponse
import com.ranjan.somiq.profile.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

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
            onSuccess = { response ->
                response.body<ProfileResponseDto>().toDomain()
            }
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
                    contentType(ContentType.Application.Json)
                    setBody(
                        UpdateProfileRequestDto(
                            name = name,
                            username = username,
                            bio = bio,
                            profilePictureUrl = profilePictureUrl
                        )
                    )
                }
            },
            onSuccess = { response ->
                response.body<ProfileResponseDto>().toDomain()
            }
        )
    }
}
