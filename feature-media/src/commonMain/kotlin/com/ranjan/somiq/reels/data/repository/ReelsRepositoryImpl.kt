package com.ranjan.somiq.reels.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.reels.data.model.Reel
import com.ranjan.somiq.reels.data.model.ReelResponse
import com.ranjan.somiq.reels.data.model.ToggleResponse
import com.ranjan.somiq.reels.domain.repository.ReelsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class ReelsRepositoryImpl(
    private val httpClient: HttpClient
) : ReelsRepository {

    override suspend fun getReels(): Result<List<Reel>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/reels") },
            onSuccess = { response -> response.body<ReelResponse>().data },
            errorMessage = "Failed to load reels"
        )
    }

    override suspend fun getReel(reelId: String): Result<Reel> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/reels/$reelId") },
            errorMessage = "Failed to load reel"
        )
    }

    override suspend fun toggleLike(reelId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/reels/$reelId/like") },
            errorMessage = "Failed to toggle like"
        )
    }

    override suspend fun toggleBookmark(reelId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/reels/$reelId/bookmark") },
            errorMessage = "Failed to toggle bookmark"
        )
    }
}
