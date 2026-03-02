package com.ranjan.somiq.auth.di

import com.ranjan.somiq.auth.ui.login.LoginViewModel
import com.ranjan.somiq.auth.ui.signup.SignupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::SignupViewModel)
    viewModelOf(::LoginViewModel)
}
