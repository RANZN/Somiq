package com.ranjan.somiq.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)