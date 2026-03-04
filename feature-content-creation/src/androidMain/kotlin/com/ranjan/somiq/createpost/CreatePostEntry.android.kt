package com.ranjan.somiq.createpost

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun CreatePostEntry(onBack: () -> Unit) {
    val viewModel: CreatePostViewModel = koinViewModel()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.toString()?.let { viewModel.handleIntent(CreatePostContract.Intent.ImagePicked(it)) }
    }
    CreatePostScreenHost(
        onBack = onBack,
        onRequestPickImage = { launcher.launch("image/*") },
        viewModel = viewModel
    )
}
