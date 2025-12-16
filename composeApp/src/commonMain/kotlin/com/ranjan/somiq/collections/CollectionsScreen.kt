package com.ranjan.somiq.collections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.collections.CollectionsContract.Action
import com.ranjan.somiq.collections.CollectionsContract.Effect
import com.ranjan.somiq.core.data.model.CollectionResponse
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    viewModel: CollectionsViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleAction(Action.LoadCollections)
    }

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.ShowError -> {
                // Handle error
            }

            is Effect.CollectionCreated -> {
                // Handle success
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collections") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Show create collection dialog */ }
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.collections.isEmpty() -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null && uiState.collections.isEmpty() -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val errorText = uiState.error
                        if (errorText != null) {
                            Text(
                                text = errorText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.handleAction(Action.Refresh) }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.collections) { collection ->
                        CollectionItem(collection = collection)
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionItem(
    collection: CollectionResponse
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = collection.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            collection.description?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${collection.itemsCount} items",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

