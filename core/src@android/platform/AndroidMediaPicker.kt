package com.ranjan.somiq.core.platform

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidMediaPicker(activity: ComponentActivity) : MediaPicker {

    private var continuation: CancellableContinuation<String?>? = null

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        continuation?.resume(uri?.toString())
        continuation = null
    }

    override suspend fun pickMedia(type: MediaType): String? = suspendCancellableCoroutine { cont ->
        continuation = cont
        
        val mimeType = when (type) {
            MediaType.IMAGE -> "image/*"
            MediaType.VIDEO -> "video/*"
        }
        
        launcher.launch(mimeType)

        cont.invokeOnCancellation {
            continuation = null
        }
    }
}
