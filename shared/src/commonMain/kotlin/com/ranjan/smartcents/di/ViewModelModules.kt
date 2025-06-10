package com.ranjan.smartcents.di

import com.ranjan.smartcents.presentation.login.LoginViewModel
import com.ranjan.smartcents.presentation.quiz.QuizViewModel
import com.ranjan.smartcents.presentation.signup.SignupViewModel
import com.ranjan.smartcents.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModules = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::QuizViewModel)
}