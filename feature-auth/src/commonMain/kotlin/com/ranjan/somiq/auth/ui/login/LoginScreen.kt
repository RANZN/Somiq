package com.ranjan.somiq.auth.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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
import com.ranjan.somiq.auth.ui.login.components.LoginHeader
import com.ranjan.somiq.core.presentation.component.CustomOutlinedButton
import com.ranjan.somiq.core.presentation.component.CustomTextField
import com.ranjan.somiq.core.presentation.component.OnboardingButton
import com.ranjan.somiq.core.presentation.util.clickWithEffect
import com.ranjan.somiq.core.presentation.util.defaultPadding
import com.ranjan.somiq.auth.ui.login.LoginContract.Action
import com.ranjan.somiq.auth.ui.login.LoginContract.UiState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(
    uiState: UiState,
    modifier: Modifier = Modifier,
    action: (Action) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .defaultPadding()
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LoginHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        )
        val isEmailError = uiState.error == UiState.Errors.INVALID_EMAIL ||
                uiState.error == UiState.Errors.INVALID_CREDENTIALS
        CustomTextField(
            value = uiState.email,
            onValueChange = { action(Action.OnEmailChange(it)) },
            placeholder = "Email or Phone",
            leadingImageVector = Icons.Outlined.Email,
            isError = isEmailError,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.semantics { contentType = ContentType.EmailAddress },
        )
        val isPasswordError =
            uiState.error == UiState.Errors.INVALID_PASSWORD ||
                    uiState.error == UiState.Errors.INVALID_CREDENTIALS
        CustomTextField(
            value = uiState.password,
            onValueChange = { action(Action.OnPasswordChange(it)) },
            placeholder = "Password",
            leadingImageVector = Icons.Outlined.Lock,
            isError = isPasswordError,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.semantics { contentType = ContentType.Password },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text("Forgot password")
        }
        OnboardingButton(
            text = "Login",
            isLoading = uiState.isLoading,
            onClick = { action(Action.Login) }
        )

        CustomOutlinedButton(
            text = "Continue with Google",
            icon = Icons.Outlined.Email,
            onClick = { action(Action.OnGoogleLoginClick) }
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account? ")
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickWithEffect {
                        action(Action.NavigateToSignUp)
                    }
                    .padding(start = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPrev() {
    LoginScreen(UiState()) {}
}