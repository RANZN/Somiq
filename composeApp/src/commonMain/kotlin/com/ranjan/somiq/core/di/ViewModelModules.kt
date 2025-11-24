package com.ranjan.somiq.core.di

import com.ranjan.somiq.auth.ui.login.LoginViewModel
import com.ranjan.somiq.auth.ui.signup.SignupViewModel
import com.ranjan.somiq.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModules = module {
    viewModelOf(::SignupViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SplashViewModel)
}