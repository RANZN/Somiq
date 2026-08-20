package com.ranjan.somiq.app.postDetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Effect
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Intent
import com.ranjan.somiq.core.presentation.util.collectEffects
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PostDetailHostScreen(
    postId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: PostDetailViewModel = koinViewModel(parameters = { parametersOf(postId) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(postId) {
        viewModel.handleIntent(Intent.Initialize)
    }
    viewModel.collectEffects { effect ->
        when (effect) {
            is Effect.CommentPosted -> {
                // Handle success
            }
        }
    }
    PostDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        modifier = modifier,
        onIntent = viewModel::handleIntent
    )
}
