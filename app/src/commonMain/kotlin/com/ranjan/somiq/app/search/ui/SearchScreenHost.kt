package com.ranjan.somiq.app.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.ranjan.somiq.app.search.ui.SearchContract.Action
import com.ranjan.somiq.app.search.ui.SearchContract.Effect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenHost(
    viewModel: SearchViewModel = koinViewModel(),
    externalSearchQuery: String? = null,
    onExternalQueryChange: (String) -> Unit = {},
    showSearchFieldInContent: Boolean = true,
    onNavigateToUser: (String) -> Unit = {},
    onNavigateToHashtag: (String) -> Unit = {},
    onNavigateToPost: (String) -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    if (externalSearchQuery != null) {
        LaunchedEffect(externalSearchQuery) {
            viewModel.handleAction(Action.OnQueryChange(externalSearchQuery))
            viewModel.handleAction(Action.PerformSearch)
        }
    }

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToUser -> onNavigateToUser(effect.userId)
            is Effect.NavigateToHashtag -> onNavigateToHashtag(effect.hashtag)
            is Effect.NavigateToPost -> onNavigateToPost(effect.postId)
        }
    }

    if (!showSearchFieldInContent && externalSearchQuery != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        TextField(
                            value = externalSearchQuery,
                            onValueChange = onExternalQueryChange,
                            placeholder = { Text("Search") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SearchScreen(
                    uiState = uiState,
                    onAction = viewModel::handleAction,
                    showSearchFieldInContent = false
                )
            }
        }
    } else {
        SearchScreen(
            uiState = uiState,
            onAction = viewModel::handleAction,
            showSearchFieldInContent = showSearchFieldInContent
        )
    }
}
