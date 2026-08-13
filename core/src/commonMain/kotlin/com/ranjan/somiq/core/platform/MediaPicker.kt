package com.ranjan.somiq.core.platform

enum class MediaType {
    IMAGE, VIDEO
}

interface MediaPicker {
    suspend fun pickMedia(type: MediaType): String?
}
