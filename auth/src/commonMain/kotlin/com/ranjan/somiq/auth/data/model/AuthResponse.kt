package com.ranjan.somiq.auth.data.model

import com.ranjan.somiq.core.data.model.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: AuthToken,
    val user: User
)

@Serializable
data class User(
    @SerialName("userId")
    val id: String,
    val name: String,
    val email: String,
)
