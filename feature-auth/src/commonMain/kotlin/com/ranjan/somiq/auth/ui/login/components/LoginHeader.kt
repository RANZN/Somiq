package com.ranjan.somiq.auth.ui.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "SomiQ",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.headlineMedium
        )
    }

}

@Preview
@Composable
private fun LoginHeaderPrev() {
    LoginHeader()
}