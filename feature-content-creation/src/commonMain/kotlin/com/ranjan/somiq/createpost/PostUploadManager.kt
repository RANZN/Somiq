package com.ranjan.somiq.createpost

import com.ranjan.somiq.core.domain.PostUploadService
import com.ranjan.somiq.core.domain.UploadState
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.platform.readUriToBytes
import com.ranjan.somiq.core.util.currentTimeMillis
import com.ranjan.somiq.feed.domain.model.CreatePostRequest
import com.ranjan.somiq.feed.domain.model.PostMedia
import com.ranjan.somiq.feed.domain.usecase.CreatePostUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostUploadManager(
    private val createPostUseCase: CreatePostUseCase
) : PostUploadService {
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    override val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    private val uploadScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun uploadPost(caption: String, imageUri: String) {
        _uploadState.value = UploadState.Uploading(caption, imageUri)
        uploadScope.launch {
            val bytes = readUriToBytes(imageUri)
            if (bytes == null || bytes.isEmpty()) {
                val appError = AppError.Custom(CreatePostContract.ScreenError.CouldNotReadImage)
                _uploadState.value = UploadState.Failed(appError)
                return@launch
            }
            val fileName = "post_${currentTimeMillis()}"

            val request = CreatePostRequest(
                caption = caption,
                mediaUrls = listOf(
                    PostMedia(
                        name = fileName,
                        byte = bytes
                    )
                ),
            )

            createPostUseCase(request).fold(
                onSuccess = {
                    _uploadState.value = UploadState.Success
                },
                onFailure = { e ->
                    val appError = e.toAppError(CreatePostContract.ScreenError.CreatePostFailed)
                    _uploadState.value = UploadState.Failed(appError)
                }
            )
        }
    }

    override fun resetToIdle() {
        _uploadState.value = UploadState.Idle
    }
}
