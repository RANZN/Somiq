package com.ranjan.somiq.profile.domain.usecase

import com.ranjan.somiq.feed.domain.usecase.GetBookmarkedPostsUseCase
import com.ranjan.somiq.feed.domain.usecase.GetPostsByUserUseCase
import com.ranjan.somiq.profile.domain.model.ProfileResponse


class LoadOwnProfileUseCase(
    private val getProfileUseCase: GetProfileUseCase,
    private val getPostsByUserUseCase: GetPostsByUserUseCase,
    private val getBookmarkedPostsUseCase: GetBookmarkedPostsUseCase
) {
    suspend operator fun invoke(): Result<ProfileResponse> {
        return getProfileUseCase(null).mapCatching { profile ->
            getPostsByUserUseCase(profile.user.id)
            getBookmarkedPostsUseCase()
            profile
        }
    }
}
