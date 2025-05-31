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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ranjan.smartcents.android.R
import com.ranjan.smartcents.android.component.BoldOutlinedButton
import com.ranjan.smartcents.android.component.OnClick
import com.ranjan.smartcents.android.util.screenDefault

@Composable
fun QuizInstruction(startQuiz: OnClick) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier.screenDefault(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(R.string.quiz_instruction),
                style = MaterialTheme.typography.displayMedium
            )
            BulletPoints(
                points = stringArrayResource(R.array.quiz_instruction_list).toList(),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            )

            BoldOutlinedButton(
                text = stringResource(R.string.start_quiz),
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