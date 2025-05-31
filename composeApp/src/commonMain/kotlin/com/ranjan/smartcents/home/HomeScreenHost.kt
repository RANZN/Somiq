package com.ranjan.smartcents.android.home

import androidx.compose.runtime.Composable
import com.ranjan.smartcents.android.component.OnClick

@Composable
fun HomeScreenHost(
    navigateToMoneyInsights: OnClick,
    navigateToMoneyQuotes: OnClick,
    navigateToLearnFinance: OnClick,
    navigateToQuizzes: OnClick,
) {
    HomeScreen(
        navigateToMoneyInsights = navigateToMoneyInsights,
        navigateToMoneyQuotes = navigateToMoneyQuotes,
        navigateToLearnFinance = navigateToLearnFinance,
        navigateToQuizzes = navigateToQuizzes,
    )
}