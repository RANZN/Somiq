package com.ranjan.smartcents.android.quiz_intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ranjan.smartcents.android.R
import com.ranjan.smartcents.android.component.VSpace
import com.ranjan.smartcents.android.quiz_intro.components.FinMasterQuizItem
import com.ranjan.smartcents.presentation.quiz.QuizType
import com.ranjan.smartcents.android.util.screenDefault

@Composable
fun FinMasterQuiz(
    navigateToQuiz: (QuizType) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.screenDefault(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(R.string.finmaster_quiz),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 20.dp),
            )
            Text(
                text = stringResource(R.string.test_your_money_skills_in_a_fun_quiz_for_students_of_all_grades),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            VSpace(10.dp)

            FinMasterQuizItem(
                icon = painterResource(R.drawable.ic_child),
                title = stringResource(R.string.finmaster_junior),
                description = stringResource(R.string.for_class_5_8)
            ) {
                navigateToQuiz(QuizType.FIN_MASTER_JUNIOR)
            }

            FinMasterQuizItem(
                icon = painterResource(R.drawable.ic_graduation_cap),
                title = stringResource(R.string.finmaster_senior),
                description = stringResource(R.string.for_class_9_10)
            ) {
                navigateToQuiz(QuizType.FIN_MASTER_SENIOR)
            }

            FinMasterQuizItem(
                icon = painterResource(R.drawable.ic_person_pro),
                title = stringResource(R.string.finmaster_pro),
                description = stringResource(R.string.for_class_11_12)
            ) {
                navigateToQuiz(QuizType.FIN_MASTER_PRO)
            }
        }
    }
}

@Preview
@Composable
private fun FinMasterQuizPrev() {
    FinMasterQuiz {}
}