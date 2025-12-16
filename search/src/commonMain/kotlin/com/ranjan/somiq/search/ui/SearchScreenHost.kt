package com.ranjan.somiq.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.search.ui.SearchContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreenHost(
    viewModel: SearchViewModel = koinViewModel(),
    onNavigateToUser: (String) -> Unit = {},
    onNavigateToHashtag: (String) -> Unit = {},
    onNavigateToPost: (String) -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToUser -> onNavigateToUser(effect.userId)
            is Effect.NavigateToHashtag -> onNavigateToHashtag(effect.hashtag)
            is Effect.NavigateToPost -> onNavigateToPost(effect.postId)
        }
    }

    SearchScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}
