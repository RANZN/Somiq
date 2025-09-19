package com.ranjan.smartcents.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)