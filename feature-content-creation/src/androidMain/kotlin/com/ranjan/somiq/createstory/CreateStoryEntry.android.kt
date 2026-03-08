package com.ranjan.somiq.createstory

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun CreateStoryEntry(onBack: () -> Unit) {
    val viewModel: CreateStoryViewModel = koinViewModel()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.toString()?.let { viewModel.handleIntent(CreateStoryContract.Intent.ImagePicked(it)) }
    }
    CreateStoryScreenHost(
        onBack = onBack,
        onRequestPickImage = { launcher.launch("image/*") },
        viewModel = viewModel
    )
}
