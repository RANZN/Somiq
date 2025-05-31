package com.ranjan.smartcents.android.quiz_intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ranjan.smartcents.android.R
import com.ranjan.smartcents.android.component.OnClick
import com.ranjan.smartcents.android.component.VSpace
import com.ranjan.smartcents.android.quiz_intro.components.QuizTypeButton
import com.ranjan.smartcents.android.util.screenDefault

@Composable
fun QuizzesScreen(
    navigateToFinMasterQuiz: OnClick,
    navigateToFounderQuiz: OnClick
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .screenDefault()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(R.string.quiz),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 20.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_quizzes),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp),
            )
            Text(
                stringResource(R.string.participate_and_compete_in_our_engaging_finance_and_business_quiz_challenges),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            VSpace(20.dp)
            QuizTypeButton(
                title = stringResource(R.string.finmaster_quiz),
                desc = stringResource(R.string.for_students_and_aspiring_professionals),
                onClick = navigateToFinMasterQuiz
            )
            QuizTypeButton(
                title = stringResource(R.string.founder_quiz),
                desc = stringResource(R.string.for_entrepreneurs_business_owners_and_working_professionals),
                onClick = navigateToFounderQuiz
            )
        }
    }
}

@Preview
@Composable
private fun QuizzesScreenPrev() {
    QuizzesScreen(
        navigateToFinMasterQuiz = {},
        navigateToFounderQuiz = {}
    )
}