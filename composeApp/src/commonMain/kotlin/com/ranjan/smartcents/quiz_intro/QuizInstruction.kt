package com.ranjan.smartcents.quiz_intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import smartcents.composeapp.generated.resources.Res
import com.ranjan.smartcents.component.BoldOutlinedButton
import com.ranjan.smartcents.component.OnClick
import com.ranjan.smartcents.util.screenDefault
import org.jetbrains.compose.resources.stringArrayResource
import smartcents.composeapp.generated.resources.*

@Composable
fun QuizInstruction(startQuiz: OnClick) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier.screenDefault(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(Res.string.quiz_instruction),
                style = MaterialTheme.typography.displayMedium
            )
            BulletPoints(
                points = stringArrayResource(Res.array.quiz_instruction_list).toList(),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            )

            BoldOutlinedButton(
                text = stringResource(Res.string.start_quiz),
                modifier = Modifier.padding(vertical = 16.dp),
                onClick = startQuiz
            )
        }
    }
}


@Composable
fun BulletPoints(
    modifier: Modifier = Modifier,
    points: List<String>
) {
    Column(modifier = modifier) {
        points.forEach { point ->
            Text(
                text = "•  $point",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
private fun QuizInstructionPrev() {
    QuizInstruction { }
}