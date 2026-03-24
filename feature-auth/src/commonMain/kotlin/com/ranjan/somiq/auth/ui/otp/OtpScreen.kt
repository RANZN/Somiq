package com.ranjan.somiq.auth.ui.otp

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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.auth.ui.components.LoginHeader
import com.ranjan.somiq.auth.ui.otp.OtpContract
import com.ranjan.somiq.auth.ui.otp.OtpContract.Intent
import com.ranjan.somiq.auth.ui.otp.OtpContract.UiState
import com.ranjan.somiq.core.presentation.component.CustomTextField
import com.ranjan.somiq.core.presentation.component.OnboardingButton
import com.ranjan.somiq.core.presentation.util.defaultPadding

@Composable
fun OtpScreen(
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
            text = "Enter code",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Sent to ${uiState.phoneDisplay}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        val remaining =
            OtpContract.MAX_OTP_FAILURES_BEFORE_LOCKOUT - uiState.failedAttempts
        if (uiState.failedAttempts in 1..<OtpContract.MAX_OTP_FAILURES_BEFORE_LOCKOUT) {
            Text(
                text = "$remaining attempt${if (remaining == 1) "" else "s"} remaining",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
        CustomTextField(
            value = uiState.otp,
            onValueChange = { intent(Intent.OnOtpChange(it)) },
            placeholder = "6-digit code (000000)",
            leadingImageVector = Icons.Outlined.Lock,
            isError = uiState.error != null,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword,
            ),
            modifier = Modifier.semantics { contentType = ContentType.SmsOtpCode },
        )
        OnboardingButton(
            text = "Verify",
            isLoading = uiState.isLoading,
            onClick = { intent(Intent.Verify) },
        )
    }
}
