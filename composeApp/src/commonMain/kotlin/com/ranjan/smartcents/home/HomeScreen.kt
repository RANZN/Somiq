package com.ranjan.smartcents.android.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ranjan.smartcents.android.R
import com.ranjan.smartcents.android.component.OnClick
import com.ranjan.smartcents.android.component.VSpace
import com.ranjan.smartcents.android.home.components.HomeHeader
import com.ranjan.smartcents.android.home.components.HomeItems
import com.ranjan.smartcents.android.util.defaultPadding

@Composable
fun HomeScreen(
    navigateToMoneyInsights: OnClick,
    navigateToMoneyQuotes: OnClick,
    navigateToLearnFinance: OnClick,
    navigateToQuizzes: OnClick,
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.defaultPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HomeHeader(Modifier.padding(20.dp))
            VSpace(20.dp)
            HomeItems(
                iconRes = R.drawable.ic_money_insights,
                text = stringResource(R.string.money_insights),
                onClick = navigateToMoneyInsights
            )
            HomeItems(
                iconRes = R.drawable.ic_money_quotes,
                text = stringResource(R.string.money_quotes),
                onClick = navigateToMoneyQuotes
            )
            HomeItems(
                iconRes = R.drawable.ic_learn_finance,
                text = stringResource(R.string.learn_finance),
                onClick = navigateToLearnFinance
            )
            HomeItems(
                iconRes = R.drawable.ic_quizzes,
                text = stringResource(R.string.quizzes),
                onClick = navigateToQuizzes
            )
        }
    }
}