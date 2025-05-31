package com.ranjan.smartcents.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
    val uid: String,
    val createdAt: String
)

@Serializable
data class UserProfile(
    val name: String,
    val email: String,
    val createdAt: String
)