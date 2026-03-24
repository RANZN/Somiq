package com.ranjan.somiq.auth.domain.usecase

import com.ranjan.somiq.auth.domain.model.VerifyOtpResult
import com.ranjan.somiq.auth.domain.repository.AuthRepository

class VerifyOtpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        phone: String,
        otp: String,
    ): VerifyOtpResult = authRepository.verifyOtp(phone, otp)
}
