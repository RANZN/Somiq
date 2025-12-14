package com.ranjan.somiq.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreenHost(
    viewModel: SearchViewModel = koinViewModel(),
    onNavigateToUser: (String) -> Unit = {},
    onNavigateToHashtag: (String) -> Unit = {},
    onNavigateToPost: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            is SearchEvent.NavigateToUser -> onNavigateToUser(event.userId)
            is SearchEvent.NavigateToHashtag -> onNavigateToHashtag(event.hashtag)
            is SearchEvent.NavigateToPost -> onNavigateToPost(event.postId)
        }
    }

    SearchScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}
