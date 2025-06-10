package com.ranjan.smartcents.quiz_intro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import smartcents.composeapp.generated.resources.Res
import com.ranjan.smartcents.component.OnClick
import com.ranjan.smartcents.util.screenDefault
import smartcents.composeapp.generated.resources.*

@Composable
fun FounderQuiz(
    startQuiz: OnClick
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.screenDefault()
        ) {
            Text(
                stringResource(Res.string.founder_quiz),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center
            )

            Icon(
                painter = painterResource(Res.drawable.ic_working_professional),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.challenge_your_business_brain_take_the_quiz_built_for_founders_innovators_and_future_leaders),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = startQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(2.dp, Color.Black),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(Res.string.start_quiz),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}

@Preview
@Composable
private fun FounderQuizPrev() {
    FounderQuiz { }
}