package com.ranjan.somiq.app.search.data.mapper

import com.ranjan.somiq.app.search.data.model.*
import com.ranjan.somiq.app.search.domain.model.SearchResult
import com.ranjan.somiq.feed.data.mapper.toDomain
import com.ranjan.somiq.profile.domain.model.User

fun SearchUserDto.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    phone = phone,
    username = username,
    profilePictureUrl = profilePictureUrl,
    bio = bio
)

fun SearchResultResponse.toDomain(): SearchResult = SearchResult(
    users = users.map { it.toDomain() },
    posts = posts.map { it.toDomain() },
    reels = reels
)
