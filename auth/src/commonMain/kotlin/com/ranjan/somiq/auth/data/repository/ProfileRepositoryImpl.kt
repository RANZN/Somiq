package com.ranjan.somiq.auth.data.repository

import com.ranjan.somiq.auth.data.model.ProfileResponse
import com.ranjan.somiq.auth.domain.repository.ProfileRepository
import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
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
        return try {
            val url = if (userId != null) {
                "$BASE_URL/v1/account/$userId"
            } else {
                "$BASE_URL/v1/account"
            }
            val response = httpClient.get(url)
            if (response.status == HttpStatusCode.OK) {
                try {
                    val profile = response.body<ProfileResponse>()
                    Result.success(profile)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse profile: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load profile: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load profile: ${e.message}"))
        }
    }

    override suspend fun updateProfile(
        name: String?,
        username: String?,
        bio: String?,
        profilePictureUrl: String?
    ): Result<ProfileResponse> {
        return try {
            val response = httpClient.put("$BASE_URL/v1/account/profile") {
                setBody(
                    UpdateProfileRequest(
                        name = name,
                        username = username,
                        bio = bio,
                        profilePictureUrl = profilePictureUrl
                    )
                )
            }
            if (response.status == HttpStatusCode.OK) {
                try {
                    val profile = response.body<ProfileResponse>()
                    Result.success(profile)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse profile: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to update profile: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update profile: ${e.message}"))
        }
    }
}

