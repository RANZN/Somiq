package com.ranjan.smartcents.signup

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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Repartition
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import smartcents.composeapp.generated.resources.Res
import com.ranjan.smartcents.component.CustomOutlinedButton
import com.ranjan.smartcents.component.CustomTextField
import com.ranjan.smartcents.component.OnboardingButton
import com.ranjan.smartcents.signup.components.SignupHeader
import com.ranjan.smartcents.util.defaultPadding
import com.ranjan.smartcents.presentation.signup.SignupViewModel.Action
import com.ranjan.smartcents.presentation.signup.SignupViewModel.UiState
import com.ranjan.smartcents.signup.mapper.getMessage
import smartcents.composeapp.generated.resources.*

@Composable
fun SignupScreen(
    uiState: UiState,
    errorMsg: String,
    action: (Action) -> Unit
) {
    val scrollState = rememberScrollState()
    Surface(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .defaultPadding()
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SignupHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

            val nameError by remember(uiState.error) {
                derivedStateOf {
                    uiState.error.find { it is UiState.Error.Name }
                }
            }
            CustomTextField(
                value = uiState.name,
                onValueChange = { action(Action.OnNameChange(it)) },
                placeholder = stringResource(Res.string.full_name),
                leadingImageVector = Icons.Outlined.Person,
                isError = nameError != null,
                errorMessage = nameError?.getMessage(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                )
            )

            val emailError by remember(uiState.error) {
                derivedStateOf {
                    uiState.error.find { it is UiState.Error.Email }
                }
            }
            CustomTextField(
                value = uiState.email,
                onValueChange = { action(Action.OnEmailChange(it)) },
                placeholder = stringResource(Res.string.email_or_phone),
                leadingImageVector = Icons.Outlined.Email,
                isError = emailError != null,
                errorMessage = emailError?.getMessage(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            val passwordError by remember(uiState.error) {
                derivedStateOf {
                    uiState.error.find { it is UiState.Error.Password }
                }
            }
            CustomTextField(
                value = uiState.password,
                onValueChange = { action(Action.OnPasswordChange(it)) },
                placeholder = stringResource(Res.string.password),
                leadingImageVector = Icons.Outlined.Password,
                isError = passwordError != null,
                errorMessage = passwordError?.getMessage(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
            )

            val rePasswordError by remember(uiState.error) {
                derivedStateOf {
                    uiState.error.find { it is UiState.Error.ConfirmPassword }
                }
            }
            CustomTextField(
                value = uiState.confirmPassword,
                onValueChange = { action(Action.OnConfirmPasswordChange(it)) },
                placeholder = stringResource(Res.string.confirm_password),
                leadingImageVector = Icons.Outlined.Repartition,
                isError = rePasswordError != null,
                errorMessage = rePasswordError?.getMessage(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
            )

            OnboardingButton(
                text = stringResource(Res.string.sign_up),
                isLoading = uiState.isLoading,
                onClick = { action(Action.Signup) }
            )
            Text(
                errorMsg,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Red,
                textAlign = TextAlign.Center
            )

            CustomOutlinedButton(
                text = stringResource(Res.string.continue_with_google),
                icon = Icons.Outlined.Email,
                onClick = { action(Action.OnGoogleLoginClick) }
            )
        }
    }
}

@Preview
@Composable
private fun SignupScreenPrev() {
    SignupScreen(UiState(), "") {}
}