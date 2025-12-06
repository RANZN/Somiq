package com.ranjan.somiq.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.core.presentation.util.screenDefault

@Composable
fun ScrollableSurface(
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.screenDefault(),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}