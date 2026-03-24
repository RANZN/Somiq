package com.ranjan.somiq.auth.domain.model

import com.ranjan.somiq.auth.data.model.User

sealed class VerifyOtpResult {
    data class LoggedIn(val user: User) : VerifyOtpResult()
    data class SignupRequired(val signupToken: String) : VerifyOtpResult()

    sealed class Failure : VerifyOtpResult() {
        object InvalidOtp : Failure()
        object AccountNotFound : Failure()
        object PhoneAlreadyRegistered : Failure()
        object NoNetwork : Failure()
        object ServerError : Failure()
        data class Unknown(val message: String?) : Failure()
    }
}
