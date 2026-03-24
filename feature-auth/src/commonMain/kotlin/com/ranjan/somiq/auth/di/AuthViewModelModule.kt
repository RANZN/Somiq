package com.ranjan.somiq.auth.di

import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileViewModel
import com.ranjan.somiq.auth.ui.otp.OtpViewModel
import com.ranjan.somiq.auth.ui.phone.PhoneEntryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::PhoneEntryViewModel)
    viewModelOf(::OtpViewModel)
    viewModelOf(::CompleteProfileViewModel)
}
