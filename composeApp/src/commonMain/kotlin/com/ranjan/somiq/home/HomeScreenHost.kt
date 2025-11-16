package com.ranjan.somiq.home

import androidx.compose.runtime.Composable
import com.ranjan.somiq.component.OnClick

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