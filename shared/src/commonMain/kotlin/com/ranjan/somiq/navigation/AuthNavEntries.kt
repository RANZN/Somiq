package com.ranjan.somiq.navigation

import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileScreenHost
import com.ranjan.somiq.auth.ui.otp.OtpScreenHost
import com.ranjan.somiq.auth.ui.phone.PhoneEntryScreenHost
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val authNavigationModule = module {

    navigation<OnBoarding.Login> {
        val backStack = LocalNavBackStack.current
        PhoneEntryScreenHost(
            navigateToOtp = { phone ->
                backStack.add(OnBoarding.Otp(phone = phone))
            },
        )
    }

    navigation<OnBoarding.Otp> { key: OnBoarding.Otp ->
        val backStack = LocalNavBackStack.current
        OtpScreenHost(
            phone = key.phone,
            navigateCompleteProfile = { signupToken ->
                backStack.add(OnBoarding.CompleteProfile(signupToken = signupToken))
            },
            navigateHome = {
                backStack.clear()
                backStack.add(HomeGraph)
            },
            navigateBackToPhone = { backStack.removeLastOrNull() },
        )
    }

    navigation<OnBoarding.CompleteProfile> { key: OnBoarding.CompleteProfile ->
        val backStack = LocalNavBackStack.current
        CompleteProfileScreenHost(
            signupToken = key.signupToken,
            navigateHome = {
                backStack.clear()
                backStack.add(HomeGraph)
            }
        )
    }
}
