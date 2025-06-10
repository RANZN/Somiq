package com.ranjan.smartcents.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OnboardingButton(
    text: String,
    modifier: Modifier = Modifier,
    isLoading : Boolean = false,
    onClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Button(
        onClick = {
            keyboardController?.hide()
            if (!isLoading) onClick()
        },
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors()
            .copy(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White),
    ) {
        if (!isLoading) Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            modifier = Modifier.padding(vertical = 4.dp)
        ) else CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}


@Preview
@Composable
private fun OnboardingButtonPrev() {
    OnboardingButton(
        text = "Login",
        onClick = {}
    )
}