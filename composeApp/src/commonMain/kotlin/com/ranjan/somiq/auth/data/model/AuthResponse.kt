package com.ranjan.somiq.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val user: User
)

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
)
