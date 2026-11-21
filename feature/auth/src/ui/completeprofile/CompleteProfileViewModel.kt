package com.ranjan.somiq.auth.ui.completeprofile

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.usecase.CheckUserIdUseCase
import com.ranjan.somiq.auth.domain.usecase.CompleteSignupUseCase
import com.ranjan.somiq.auth.domain.usecase.UploadProfilePictureUseCase
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.Effect
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.Intent
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.UiState
import com.ranjan.somiq.core.platform.MediaPicker
import com.ranjan.somiq.core.platform.MediaType
import com.ranjan.somiq.core.platform.readUriToBytes
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.error.toUiText
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.model.resolve
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.could_not_check_username
import com.ranjan.somiq.core.resources.email_already_in_use
import com.ranjan.somiq.core.resources.please_wait_username_check
import com.ranjan.somiq.core.resources.session_expired_start_again
import com.ranjan.somiq.core.util.currentTimeMillis
import com.ranjan.somiq.core.util.isValidEmail
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompleteProfileViewModel(
    private val signupToken: String,
    private val completeSignupUseCase: CompleteSignupUseCase,
    private val checkUserIdUseCase: CheckUserIdUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
    private val mediaPicker: MediaPicker,
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private var userIdDebounceJob: Job? = null
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.OnNameChange -> _uiState.update {it.copy(name = intent.name) }
                is Intent.OnUserIdChange -> handleUserIdChange(intent.userId)
                is Intent.OnUserIdFocusChanged -> handleUserIdFocus(intent.isFocused)
                is Intent.OnEmailChange -> _uiState.update {it.copy(email = intent.email) }
                Intent.AddPhotoClick -> {
                    viewModelScope.launch {
                        val uri = mediaPicker.pickMedia(MediaType.IMAGE)
                        if (uri != null) {
                            _uiState.update {it.copy(selectedLocalImageUri = uri, error = emptyList()) }
                        }
                    }
                }
                Intent.Submit -> handleSubmit()
            }
        }
    }
    private fun handleUserIdChange(userId: String) {
        _uiState.update {it.copy(userId = userId, userIdAvailable = null) }
        userIdDebounceJob?.cancel()
        val uid = userId.trim()
        if (uid.length < MIN_USERNAME_LEN_FOR_CHECK) {
            _uiState.update {it.copy(isCheckingUserId = false) }
            return
        }
        if (validateUserIdOnly(uid).isNotEmpty()) {
            _uiState.update {it.copy(userIdAvailable = null, isCheckingUserId = false) }
            return
        }
        userIdDebounceJob = viewModelScope.launch {
            delay(USERNAME_CHECK_DEBOUNCE_MS.milliseconds)
            runUserIdAvailabilityCheck()
        }
    }
    private fun handleUserIdFocus(isFocused: Boolean) {
        if (isFocused) return
        userIdDebounceJob?.cancel()
        viewModelScope.launch {
            runUserIdAvailabilityCheck()
        }
    }
    private suspend fun runUserIdAvailabilityCheck() {
        val uid = uiState.value.userId.trim()
        if (uid.length < MIN_USERNAME_LEN_FOR_CHECK) {
            _uiState.update {it.copy(isCheckingUserId = false) }
            return
        }
        if (validateUserIdOnly(uid).isNotEmpty()) {
            _uiState.update {it.copy(userIdAvailable = null, isCheckingUserId = false) }
            return
        }
        _uiState.update {it.copy(isCheckingUserId = true, error = emptyList()) }
        val result = checkUserIdUseCase(uid)
        result.fold(
            onSuccess = { available ->
                _uiState.update {it.copy(
                        isCheckingUserId = false,
                        userIdAvailable = available,
                        error = if (available) emptyList() else listOf(UiState.Error.UserId.Unavailable),
                    )
                }
            },
            onFailure = {
                _uiState.update {it.copy(isCheckingUserId = false, userIdAvailable = null) }
                showSnackbar(UiText.Resource(Res.string.could_not_check_username))
            }
        )
    }
    private fun validateUserIdOnly(userId: String): List<UiState.Error> {
        val errors = mutableListOf<UiState.Error>()
        if (userId.isEmpty()) errors.add(UiState.Error.UserId.Required)
        else if (userId.length < 3) errors.add(UiState.Error.UserId.TooShort)
        else if (!userId.matches(Regex("^[a-zA-Z0-9_]+$"))) errors.add(UiState.Error.UserId.InvalidFormat)
        return errors
    }
    private fun validateLocalFields(uiState: UiState): List<UiState.Error> {
        val errors = mutableListOf<UiState.Error>()
        if (uiState.name.isBlank()) errors.add(UiState.Error.Name.Required)
        else if (uiState.name.length < 2) errors.add(UiState.Error.Name.TooShort)
        val userIdFormatErrors = validateUserIdOnly(uiState.userId.trim())
        errors.addAll(userIdFormatErrors)
        if (uiState.email.isNotBlank() && !uiState.email.isValidEmail()) {
            errors.add(UiState.Error.Email.InvalidFormat)
        }
        if (userIdFormatErrors.isEmpty()) {
            when (uiState.userIdAvailable) {
                false -> errors.add(UiState.Error.UserId.Unavailable)
                true -> Unit
                null -> errors.add(UiState.Error.UserId.AvailabilityNotChecked)
            }
        }
        return errors
    }
    private suspend fun handleSubmit() {
        if (uiState.value.isCheckingUserId) {
            showSnackbar(UiText.Resource(Res.string.please_wait_username_check))
            return
        }
        val errors = validateLocalFields(uiState.value)
        if (errors.isNotEmpty()) {
            _uiState.update {it.copy(error = errors) }
            errors.firstOrNull()?.let { showSnackbar(it.getMessage()) }
            return
        }
        _uiState.update {it.copy(isLoading = true, error = emptyList()) }
        // Step 1: Upload image if selected
        val localImageUri = uiState.value.selectedLocalImageUri
        var uploadedUrl = uiState.value.profilePictureUrl
        if (!localImageUri.isNullOrBlank()) {
            val bytes = readUriToBytes(localImageUri)
            if (bytes == null || bytes.isEmpty()) {
                val err = UiState.Error.GenericError("Could not read selected profile picture.")
                _uiState.update {it.copy(isLoading = false, error = listOf(err)) }
                showSnackbar(err.getMessage())
                return
            }
            val uploadResult = uploadProfilePictureUseCase(
                signupToken = signupToken,
                imageBytes = bytes,
                fileName = "profile_${currentTimeMillis()}.jpg"
            )
            uploadResult.fold(
                onSuccess = { serverUrl ->
                    uploadedUrl = serverUrl
                    _uiState.update {it.copy(profilePictureUrl = serverUrl) }
                },
                onFailure = { error ->
                    val appError = error.toAppError(CompleteProfileContract.ScreenError.UploadImageFailed)
                    val err = UiState.Error.GenericError(appError.toUiText().resolve())
                    _uiState.update {it.copy(isLoading = false, error = listOf(err)) }
                    showSnackbar(err.getMessage())
                    return
                }
            )
        }
        // Step 2: Complete signup
        runCompleteSignup(uploadedUrl)
    }
    private suspend fun runCompleteSignup(profilePictureUrl: String?) {
        val s = uiState.value
        val result = completeSignupUseCase(
            signupToken = signupToken,
            name = s.name.trim(),
            userId = s.userId.trim(),
            email = s.email.trim().takeIf { it.isNotEmpty() },
            profilePictureUrl = profilePictureUrl,
        )
        when (result) {
            is AuthResult.Success -> {
                _uiState.update {it.copy(isLoading = false) }
                emitEffect(Effect.NavigateHome)
            }
            AuthResult.Failure.PhoneAlreadyInUse -> {
                _uiState.update {it.copy(isLoading = false) }
                showSnackbar(UiText.Resource(Res.string.session_expired_start_again))
            }
            AuthResult.Failure.UsernameAlreadyInUse -> {
                val err = UiState.Error.UserId.AlreadyInUse
                _uiState.update {it.copy(isLoading = false, error = listOf(err)) }
                showSnackbar(err.getMessage())
            }
            AuthResult.Failure.EmailAlreadyInUse -> {
                _uiState.update {it.copy(isLoading = false) }
                showSnackbar(UiText.Resource(Res.string.email_already_in_use))
            }
            is AuthResult.Failure.Unknown -> {
                val err = UiState.Error.GenericError(result.message)
                _uiState.update {it.copy(isLoading = false, error = listOf(err)) }
                showSnackbar(err.getMessage())
            }
            else -> _uiState.update {it.copy(isLoading = false) }
        }
    }
    private companion object {
        const val USERNAME_CHECK_DEBOUNCE_MS = 500L
        /** Call check-user-id when username length is at least this (after trim). */
        const val MIN_USERNAME_LEN_FOR_CHECK = 3
    }
}
