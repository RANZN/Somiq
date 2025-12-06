package com.ranjan.somiq.core.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    placeholder: String,
    isError: Boolean = false,
    errorMessage: String? = "",
    leadingImageVector: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val shape = MaterialTheme.shapes.large
    val isPassword = remember { keyboardOptions.keyboardType == KeyboardType.Password }
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape),
            placeholder = {
                Text(text = placeholder, maxLines = 1)
            },
            shape = shape,
            isError = isError,
            leadingIcon = leadingImageVector?.let {
                { Icon(imageVector = it, contentDescription = null) }
            },
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (!isPassword) return@OutlinedTextField
                val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
        Text(
            text = errorMessage ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .padding(start = 16.dp)
                .alpha(if (isError) 1f else 0f)
        )
    }
}


@Preview
@Composable
private fun CustomTextFiledPrev() {
    CustomTextField(
        value = "",
        onValueChange = {},
        placeholder = "Enter your email",
        leadingImageVector = Icons.Default.MailOutline
    )
}