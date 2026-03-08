package com.ranjan.somiq.core.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> PaginatedLazyList(
    items: List<T>,
    isLoading: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    key: ((item: T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    topContent: LazyListScope.() -> Unit = {},
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) {
    val isAtBottom by remember {
        derivedStateOf { listState.isAtBottom() }
    }
    LaunchedEffect(isAtBottom) {
        if (isAtBottom && !isLoading && hasMore && items.isNotEmpty()) {
            onLoadMore()
        }
    }

    @Composable
    fun ListContent(contentModifier: Modifier) {
        LazyColumn(
            state = listState,
            modifier = contentModifier,
            contentPadding = contentPadding,
        ) {
            topContent()
            items(items = items, key = key, itemContent = itemContent)
            if (isLoading) {
                item(key = "paging_loader") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    if (onRefresh != null) {
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier = modifier,
        ) {
            ListContent(Modifier.fillMaxSize())
        }
    } else {
        ListContent(modifier)
    }
}

fun LazyListState.isAtBottom(offset: Int = 2): Boolean {
    if (layoutInfo.totalItemsCount == 0) return false
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return false
    return lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - offset
}
