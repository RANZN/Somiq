package com.ranjan.somiq.quiz_intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import somiq.composeapp.generated.resources.Res
import com.ranjan.somiq.component.VSpace
import com.ranjan.somiq.quiz_intro.components.FinMasterQuizItem
import com.ranjan.somiq.presentation.quiz.QuizType
import com.ranjan.somiq.util.screenDefault
import somiq.composeapp.generated.resources.*

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
                stringResource(Res.string.finmaster_quiz),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 20.dp),
            )
            Text(
                text = stringResource(Res.string.test_your_money_skills_in_a_fun_quiz_for_students_of_all_grades),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            VSpace(10.dp)

            FinMasterQuizItem(
                icon = painterResource(Res.drawable.ic_child),
                title = stringResource(Res.string.finmaster_junior),
                description = stringResource(Res.string.for_class_5_8)
            ) {
                navigateToQuiz(QuizType.FIN_MASTER_JUNIOR)
            }

            FinMasterQuizItem(
                icon = painterResource(Res.drawable.ic_graduation_cap),
                title = stringResource(Res.string.finmaster_senior),
                description = stringResource(Res.string.for_class_9_10)
            ) {
                navigateToQuiz(QuizType.FIN_MASTER_SENIOR)
            }

            FinMasterQuizItem(
                icon = painterResource(Res.drawable.ic_person_pro),
                title = stringResource(Res.string.finmaster_pro),
                description = stringResource(Res.string.for_class_11_12)
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