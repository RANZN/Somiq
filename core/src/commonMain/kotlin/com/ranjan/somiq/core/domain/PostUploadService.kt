package com.ranjan.somiq.core.domain

import kotlinx.coroutines.flow.StateFlow
import com.ranjan.somiq.core.presentation.error.AppError

sealed interface UploadState {
    data object Idle : UploadState
    data class Uploading(val caption: String, val imageUri: String) : UploadState
    data object Success : UploadState
    data class Failed(val error: AppError) : UploadState
}

interface PostUploadService {
    val uploadState: StateFlow<UploadState>
    fun uploadPost(caption: String, imageUri: String)
    fun resetToIdle()
}
