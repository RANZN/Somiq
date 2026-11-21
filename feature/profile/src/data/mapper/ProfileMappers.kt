package com.ranjan.somiq.profile.data.mapper

import com.ranjan.somiq.profile.data.model.*
import com.ranjan.somiq.profile.domain.model.*

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    phone = phone,
    username = username,
    profilePictureUrl = profilePictureUrl,
    bio = bio
)

fun ProfileResponseDto.toDomain(): ProfileResponse = ProfileResponse(
    user = user.toDomain(),
    postsCount = postsCount,
    reelsCount = reelsCount,
    followersCount = followersCount,
    followingCount = followingCount,
    isFollowing = isFollowing
)

fun UpdateProfileRequest.toDto(): UpdateProfileRequestDto = UpdateProfileRequestDto(
    name = name,
    username = username,
    bio = bio,
    profilePictureUrl = profilePictureUrl
)
