package com.ranjan.somiq.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.component.OnClick
import com.ranjan.somiq.component.VSpace
import com.ranjan.somiq.home.components.HomeHeader
import com.ranjan.somiq.home.components.HomeItems
import com.ranjan.somiq.util.defaultPadding
import org.jetbrains.compose.resources.stringResource
import somiq.composeapp.generated.resources.Res
import somiq.composeapp.generated.resources.*

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
                iconRes = Res.drawable.ic_money_insights,
                text = stringResource(Res.string.money_insights),
                onClick = navigateToMoneyInsights
            )
            HomeItems(
                iconRes = Res.drawable.ic_money_quotes,
                text = stringResource(Res.string.money_quotes),
                onClick = navigateToMoneyQuotes
            )
            HomeItems(
                iconRes = Res.drawable.ic_learn_finance,
                text = stringResource(Res.string.learn_finance),
                onClick = navigateToLearnFinance
            )
            HomeItems(
                iconRes = Res.drawable.ic_quizzes,
                text = stringResource(Res.string.quizzes),
                onClick = navigateToQuizzes
            )
        }
    }
}