package com.ranjan.smartcents.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ranjan.smartcents.presentation.login.LoginViewModel.LoginAction
import smartcents.composeapp.generated.resources.Res
import smartcents.composeapp.generated.resources.*
import com.ranjan.smartcents.component.CustomOutlinedButton
import com.ranjan.smartcents.component.CustomTextField
import com.ranjan.smartcents.component.OnboardingButton
import com.ranjan.smartcents.login.components.LoginHeader
import com.ranjan.smartcents.util.defaultPadding
import com.ranjan.smartcents.presentation.login.LoginViewModel
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import com.ranjan.smartcents.util.clickWithEffect

@Composable
fun LoginScreen(
    uiState: LoginViewModel.LoginState,
    action: (LoginAction) -> Unit
) {
    val scrollState = rememberScrollState()
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
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
            val isEmailError = uiState.error == LoginViewModel.LoginState.Errors.INVALID_EMAIL ||
                    uiState.error == LoginViewModel.LoginState.Errors.INVALID_CREDENTIALS
            CustomTextField(
                value = uiState.email,
                onValueChange = { action(LoginAction.OnEmailChange(it)) },
                placeholder = stringResource(Res.string.email_or_phone),
                leadingImageVector = Icons.Outlined.Email,
                isError = isEmailError,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            val isPasswordError =
                uiState.error == LoginViewModel.LoginState.Errors.INVALID_PASSWORD ||
                        uiState.error == LoginViewModel.LoginState.Errors.INVALID_CREDENTIALS
            CustomTextField(
                value = uiState.password,
                onValueChange = { action(LoginAction.OnPasswordChange(it)) },
                placeholder = stringResource(Res.string.password),
                leadingImageVector = Icons.Outlined.Lock,
                isError = isPasswordError,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(stringResource(Res.string.forgot_password))
            }
            OnboardingButton(
                text = stringResource(Res.string.login),
                isLoading = uiState.isLoading,
                onClick = { action(LoginAction.Login) }
            )


            CustomOutlinedButton(
                text = stringResource(Res.string.continue_with_google),
                icon = Icons.Outlined.Email,
                onClick = { action(LoginAction.OnGoogleLoginClick) }
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(stringResource(Res.string.don_t_have_an_account))
                Text(
                    text = stringResource(Res.string.sign_up),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickWithEffect {
                            action(LoginAction.NavigateToSignUp)
                        }
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPrev() {
    LoginScreen(LoginViewModel.LoginState()) {}
}