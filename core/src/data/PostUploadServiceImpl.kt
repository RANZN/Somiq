package com.ranjan.somiq.core.data

import com.ranjan.somiq.core.domain.PostUploadService
import com.ranjan.somiq.core.domain.UploadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PostUploadServiceImpl : PostUploadService {

    override val uploadState: StateFlow<UploadState> = MutableStateFlow(UploadState.Idle)

    override fun uploadPost(caption: String, imageUris: List<String>) {

    }

    override fun cancelUpload() {

    }
}