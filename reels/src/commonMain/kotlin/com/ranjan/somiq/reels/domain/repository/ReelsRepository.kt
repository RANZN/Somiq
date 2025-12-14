package com.ranjan.somiq.reels.domain.repository

import com.ranjan.somiq.reels.data.model.Reel
import com.ranjan.somiq.reels.data.model.ToggleResponse

interface ReelsRepository {
    suspend fun getReels(): Result<List<Reel>>
    suspend fun getReel(reelId: String): Result<Reel>
    suspend fun toggleLike(reelId: String): Result<ToggleResponse>
    suspend fun toggleBookmark(reelId: String): Result<ToggleResponse>
}
