package com.smartcents.server.domain.model

import com.smartcents.server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val email: String,
    val hashedPassword: String,
) {
    fun asResponse() = UserResponse(
        id = id.toString(),
        name = name,
        email = email
    )
}

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
)