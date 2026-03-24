package com.ranjan.somiq.auth.ui.completeprofile

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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.Intent
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.UiState
import com.ranjan.somiq.auth.ui.components.CompleteProfileHeader
import com.ranjan.somiq.auth.ui.components.OptionalProfileAvatar
import com.ranjan.somiq.core.presentation.component.CustomTextField
import com.ranjan.somiq.core.presentation.component.OnboardingButton
import com.ranjan.somiq.core.presentation.util.defaultPadding

@Composable
fun CompleteProfileScreen(
    uiState: UiState,
    modifier: Modifier = Modifier,
    intent: (Intent) -> Unit,
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
        CompleteProfileHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
        Text(
            text = "Your profile",
            style = MaterialTheme.typography.headlineSmall,
        )

        val nameError by remember(uiState.error) {
            derivedStateOf { uiState.error.find { it is UiState.Error.Name } }
        }
        CustomTextField(
            value = uiState.name,
            onValueChange = { intent(Intent.OnNameChange(it)) },
            placeholder = "Name",
            leadingImageVector = Icons.Outlined.Person,
            isError = nameError != null,
            errorMessage = nameError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = true,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.semantics { contentType = ContentType.PersonFullName },
        )

        OptionalProfileAvatar(
            name = uiState.name,
            profilePictureUrl = uiState.profilePictureUrl,
            onClick = { intent(Intent.AddPhotoClick) },
            modifier = Modifier.fillMaxWidth(),
        )

        val userIdError by remember(uiState.error) {
            derivedStateOf { uiState.error.find { it is UiState.Error.UserId } }
        }
        CustomTextField(
            value = uiState.userId,
            onValueChange = { intent(Intent.OnUserIdChange(it)) },
            onFocusChanged = { intent(Intent.OnUserIdFocusChanged(it.isFocused)) },
            placeholder = "Username (unique)",
            leadingImageVector = Icons.Outlined.Person,
            isError = userIdError != null,
            errorMessage = userIdError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.NewUsername },
        )
        when {
            uiState.isCheckingUserId -> Text(
                text = "Checking username…",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            uiState.userIdAvailable == true -> Text(
                text = "Username available",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
            uiState.userIdAvailable == false -> Text(
                text = "Username not available",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        val emailError by remember(uiState.error) {
            derivedStateOf { uiState.error.find { it is UiState.Error.Email } }
        }
        CustomTextField(
            value = uiState.email,
            onValueChange = { intent(Intent.OnEmailChange(it)) },
            placeholder = "Email (optional)",
            leadingImageVector = Icons.Outlined.Email,
            isError = emailError != null,
            errorMessage = emailError?.getMessage(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            modifier = Modifier.semantics { contentType = ContentType.EmailAddress },
        )

        OnboardingButton(
            text = "Save and continue",
            isLoading = uiState.isLoading,
            onClick = { intent(Intent.Submit) },
        )
    }
}

@Preview
@Composable
private fun CompleteProfilePrev() {
    CompleteProfileScreen(UiState(), modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {}
}
