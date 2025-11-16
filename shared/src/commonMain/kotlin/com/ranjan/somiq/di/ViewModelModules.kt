package com.ranjan.somiq.di

import com.ranjan.somiq.presentation.login.LoginViewModel
import com.ranjan.somiq.presentation.quiz.QuizViewModel
import com.ranjan.somiq.presentation.signup.SignupViewModel
import com.ranjan.somiq.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModules = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::QuizViewModel)
}