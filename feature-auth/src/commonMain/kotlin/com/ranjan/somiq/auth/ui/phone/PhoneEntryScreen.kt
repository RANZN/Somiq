package com.ranjan.somiq.auth.ui.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.auth.ui.components.LoginHeader
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.Intent
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.UiState
import com.ranjan.somiq.core.presentation.component.OnboardingButton
import com.ranjan.somiq.core.presentation.component.CustomTextField
import com.ranjan.somiq.core.presentation.util.defaultPadding

@Composable
fun PhoneEntryScreen(
    uiState: UiState,
    modifier: Modifier = Modifier,
    intent: (Intent) -> Unit,
) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .defaultPadding()
            .verticalScroll(scroll)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LoginHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        )
        Text(
            text = "Get started",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "We’ll text you a code to verify your number.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        val isError = uiState.error != null
        CustomTextField(
            value = uiState.phone,
            onValueChange = { intent(Intent.OnPhoneChange(it)) },
            placeholder = "Phone number",
            leadingImageVector = Icons.Outlined.Phone,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Phone,
            ),
            modifier = Modifier.semantics { contentType = ContentType.PhoneNumber },
        )
        OnboardingButton(
            text = "Continue",
            isLoading = uiState.isLoading,
            onClick = { intent(Intent.Continue) },
        )
    }
}
