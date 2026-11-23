package com.ranjan.somiq.core.domain

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow
import com.ranjan.somiq.core.presentation.error.AppError

@Stable
sealed interface UploadState {
    data object Idle : UploadState
    data class Uploading(val caption: String, val imageUris: List<String>) : UploadState
    data object Success : UploadState
    data class Failed(val error: AppError) : UploadState
}

interface PostUploadService {
    val uploadState: StateFlow<UploadState>
    fun uploadPost(caption: String, imageUris: List<String>)
    fun cancelUpload()
}
