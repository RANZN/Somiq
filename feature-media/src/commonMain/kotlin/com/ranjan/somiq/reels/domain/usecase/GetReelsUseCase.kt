package com.ranjan.somiq.reels.domain.usecase

import com.ranjan.somiq.reels.data.model.Reel
import com.ranjan.somiq.reels.domain.repository.ReelsRepository

class GetReelsUseCase(
    private val reelsRepository: ReelsRepository
) {
    suspend operator fun invoke(): Result<List<Reel>> = reelsRepository.getReels()
}
