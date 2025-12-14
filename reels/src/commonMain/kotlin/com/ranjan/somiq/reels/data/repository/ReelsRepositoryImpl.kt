package com.ranjan.somiq.reels.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.reels.data.model.Reel
import com.ranjan.somiq.reels.data.model.ReelResponse
import com.ranjan.somiq.reels.domain.repository.ReelsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode

class ReelsRepositoryImpl(
    private val httpClient: HttpClient
) : ReelsRepository {

    override suspend fun getReels(): Result<List<Reel>> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/reels")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val reelsResponse = response.body<ReelResponse>()
                    Result.success(reelsResponse.data)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse reels: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load reels: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load reels: ${e.message}"))
        }
    }

    override suspend fun getReel(reelId: String): Result<Reel> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/reels/$reelId")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val reel = response.body<Reel>()
                    Result.success(reel)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse reel: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load reel: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load reel: ${e.message}"))
        }
    }

    override suspend fun toggleLike(reelId: String): Result<Boolean> {
        return try {
            val response = httpClient.post("$BASE_URL/v1/reels/$reelId/like")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleBookmark(reelId: String): Result<Boolean> {
        return try {
            val response = httpClient.post("$BASE_URL/v1/reels/$reelId/bookmark")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
