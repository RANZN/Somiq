package com.ranjan.somiq.auth.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Repartition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.auth.ui.signup.SignUpUiState.Error
import com.ranjan.somiq.auth.ui.signup.components.AddPhotoPlaceHolder
import com.ranjan.somiq.auth.ui.signup.components.SignupHeader
import com.ranjan.somiq.auth.ui.signup.mapper.getMessage
import com.ranjan.somiq.core.presentation.component.CustomOutlinedButton
import com.ranjan.somiq.core.presentation.component.CustomTextField
import com.ranjan.somiq.core.presentation.component.OnboardingButton
import com.ranjan.somiq.core.presentation.util.defaultPadding
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SignupScreen(
    uiState: SignUpUiState,
    modifier: Modifier = Modifier,
    action: (SignUpAction) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
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

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AddPhotoPlaceHolder(onClick = { action(SignUpAction.AddPictureClick) })
        }

        val nameError by remember(uiState.error) {
            derivedStateOf {
                uiState.error.find { it is Error.Name }
            }
        }
        CustomTextField(
            value = uiState.name,
            onValueChange = { action(SignUpAction.OnNameChange(it)) },
            placeholder = "Full Name",
            leadingImageVector = Icons.Outlined.Person,
            isError = nameError != null,
            errorMessage = nameError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.semantics { contentType = ContentType.PersonFirstName },
        )

        val usernameError by remember(uiState.error) {
            derivedStateOf {
                uiState.error.find { it is Error.Username }
            }
        }
        CustomTextField(
            value = uiState.username,
            onValueChange = { action(SignUpAction.OnUsernameChange(it)) },
            placeholder = "Username",
            leadingImageVector = Icons.Outlined.Person,
            isError = usernameError != null,
            errorMessage = usernameError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.semantics {
                contentType = ContentType.NewUsername
            },
        )

        val emailError by remember(uiState.error) {
            derivedStateOf {
                uiState.error.find { it is Error.Email }
            }
        }
        CustomTextField(
            value = uiState.email,
            onValueChange = { action(SignUpAction.OnEmailChange(it)) },
            placeholder = "Email or Phone",
            leadingImageVector = Icons.Outlined.Email,
            isError = emailError != null,
            errorMessage = emailError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.semantics { contentType = ContentType.EmailAddress },
        )
        val passwordError by remember(uiState.error) {
            derivedStateOf {
                uiState.error.find { it is Error.Password }
            }
        }
        CustomTextField(
            value = uiState.password,
            onValueChange = { action(SignUpAction.OnPasswordChange(it)) },
            placeholder = "Password",
            leadingImageVector = Icons.Outlined.Password,
            isError = passwordError != null,
            errorMessage = passwordError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.semantics { contentType = ContentType.Password },
        )

        val rePasswordError by remember(uiState.error) {
            derivedStateOf {
                uiState.error.find { it is Error.ConfirmPassword }
            }
        }
        CustomTextField(
            value = uiState.confirmPassword,
            onValueChange = { action(SignUpAction.OnConfirmPasswordChange(it)) },
            placeholder = "Confirm Password",
            leadingImageVector = Icons.Outlined.Repartition,
            isError = rePasswordError != null,
            errorMessage = rePasswordError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.semantics { contentType = ContentType.Password },
        )

        OnboardingButton(
            text = "Sign Up",
            isLoading = uiState.isLoading,
            onClick = { action(SignUpAction.Signup) }
        )

        CustomOutlinedButton(
            text = "Continue with Google",
            icon = Icons.Outlined.Email,
            onClick = { action(SignUpAction.OnGoogleLoginClick) }
        )
    }
}

@Preview
@Composable
private fun SignupScreenPrev() {
    SignupScreen(
        SignUpUiState(),
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {}
}