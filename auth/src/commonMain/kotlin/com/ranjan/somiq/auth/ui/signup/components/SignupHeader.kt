package com.ranjan.somiq.auth.ui.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignupHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Somiq",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp)
        )
        Text(
            text = "Create your account",
            style = MaterialTheme.typography.headlineMedium
        )
    }

}

@Preview
@Composable
private fun SignupHeaderPrev() {
    _root_ide_package_.com.ranjan.somiq.auth.ui.signup.components.SignupHeader()
}