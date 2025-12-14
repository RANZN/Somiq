package com.ranjan.somiq.reels.domain.repository

import com.ranjan.somiq.reels.data.model.Reel

interface ReelsRepository {
    suspend fun getReels(): Result<List<Reel>>
    suspend fun getReel(reelId: String): Result<Reel>
    suspend fun toggleLike(reelId: String): Result<Boolean>
    suspend fun toggleBookmark(reelId: String): Result<Boolean>
}
