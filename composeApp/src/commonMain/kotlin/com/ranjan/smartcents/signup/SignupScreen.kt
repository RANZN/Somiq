package com.ranjan.smartcents.android.signup

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ranjan.smartcents.android.R
import com.ranjan.smartcents.android.component.CustomOutlinedButton
import com.ranjan.smartcents.android.component.CustomTextField
import com.ranjan.smartcents.android.component.OnboardingButton
import com.ranjan.smartcents.android.signup.components.SignupHeader
import com.ranjan.smartcents.android.util.defaultPadding
import com.ranjan.smartcents.presentation.signup.SignupViewModel
import com.ranjan.smartcents.presentation.signup.SignupViewModel.Action

@Composable
fun SignupScreen(
    uiState: SignupViewModel.UiState,
    errorMsg: String,
    action: (Action) -> Unit
) {
    val scrollState = rememberScrollState()
    Surface (
        Modifier.fillMaxSize()
    ){
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
                    uiState.error.find { it.type == SignupViewModel.UiState.Error.NAME }
                }
            }
            CustomTextField(
                value = uiState.name,
                onValueChange = { action(Action.OnNameChange(it)) },
                placeholder = stringResource(R.string.full_name),
                leadingImageVector = Icons.Outlined.Person,
                isError = nameError != null,
                errorMessage = nameError?.message,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                )
            )

            val emailError by remember(uiState.error) {
                derivedStateOf {
                    uiState.error.find { it.type == SignupViewModel.UiState.Error.EMAIL }
                }
            }
            CustomTextField(
                value = uiState.email,
                onValueChange = { action(Action.OnEmailChange(it)) },
                placeholder = stringResource(R.string.email_or_phone),
                leadingImageVector = Icons.Outlined.Email,
                isError = emailError != null,
                errorMessage = emailError?.message,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            val passwordError by remember(uiState.error) {
                derivedStateOf {
                    uiState.error.find { it.type == SignupViewModel.UiState.Error.PASSWORD }
                }
            }
            CustomTextField(
                value = uiState.password,
                onValueChange = { action(Action.OnPasswordChange(it)) },
                placeholder = stringResource(R.string.password),
                leadingImageVector = Icons.Outlined.Password,
                isError = passwordError!=null,
                errorMessage = passwordError?.message,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
            )

            val rePasswordError by remember(uiState.error) {
                derivedStateOf {
                    uiState.error.find { it.type == SignupViewModel.UiState.Error.RE_PASSWORD }
                }
            }
            CustomTextField(
                value = uiState.confirmPassword,
                onValueChange = { action(Action.OnConfirmPasswordChange(it)) },
                placeholder = stringResource(R.string.confirm_password),
                leadingImageVector = Icons.Outlined.Repartition,
                isError = rePasswordError != null,
                errorMessage = rePasswordError?.message,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
            )

            OnboardingButton(
                text = stringResource(R.string.sign_up),
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
                text = stringResource(R.string.continue_with_google),
                icon = Icons.Outlined.Email,
                onClick = { action(Action.OnGoogleLoginClick) }
            )
        }
    }
}

@Preview
@Composable
private fun SignupScreenPrev() {
    SignupScreen(SignupViewModel.UiState(),"") {}
}