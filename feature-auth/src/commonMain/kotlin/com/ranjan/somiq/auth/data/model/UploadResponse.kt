package com.ranjan.somiq.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    @SerialName("url") val url: String
)
