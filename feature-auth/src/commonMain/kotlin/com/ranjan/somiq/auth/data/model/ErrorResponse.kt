package com.ranjan.somiq.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)
