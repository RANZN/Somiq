package com.ranjan.somiq.createstory

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
actual fun CreateStoryEntry(onBack: () -> Unit) {
    var pendingCallback by remember { mutableStateOf<((String) -> Unit)?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.toString()?.let { pendingCallback?.invoke(it) }
        pendingCallback = null
    }
    CreateStoryScreenHost(
        onBack = onBack,
        onRequestPickImage = { onResult ->
            pendingCallback = onResult
            launcher.launch("image/*")
        }
    )
}
