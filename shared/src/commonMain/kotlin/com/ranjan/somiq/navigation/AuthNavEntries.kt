package com.ranjan.somiq.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileScreenHost
import com.ranjan.somiq.auth.ui.otp.OtpScreenHost
import com.ranjan.somiq.auth.ui.phone.PhoneEntryScreenHost
import com.ranjan.somiq.navigation.AppNavGraph.HomeGraph
import com.ranjan.somiq.navigation.AppNavGraph.OnBoarding


fun EntryProviderScope<NavKey>.authEntries(backStack: NavBackStack<NavKey>) {

    entry<OnBoarding.Login> {
        PhoneEntryScreenHost(
            navigateToOtp = { phone ->
                backStack.add(OnBoarding.Otp(phone = phone))
            },
        )
    }

    entry<OnBoarding.Otp> { key: OnBoarding.Otp ->
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

    entry<OnBoarding.CompleteProfile> { key: OnBoarding.CompleteProfile ->
        CompleteProfileScreenHost(
            signupToken = key.signupToken,
            navigateHome = {
                backStack.clear()
                backStack.add(HomeGraph)
            }
        )
    }
}
