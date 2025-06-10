package com.ranjan.smartcents.quiz_intro.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import smartcents.composeapp.generated.resources.Res
import com.ranjan.smartcents.component.OnClick
import smartcents.composeapp.generated.resources.*

@Composable
fun FinMasterQuizItem(
    icon: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: OnClick
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.weight(0.2f),
            tint = Color.Unspecified
        )
        Column(
            modifier = Modifier
                .weight(0.7f)
                .padding(start = 8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedButton(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Text(stringResource(Res.string.start_quiz), color = Color.Black)
            }
        }
    }
}

@Preview
@Composable
private fun FinMasterQuizItemPrev() {
    FinMasterQuizItem(
        icon = painterResource(Res.drawable.ic_quizzes),
        title = "FinMaster Quiz",
        description = "Test your finance knowledge",
        onClick = {}
    )
}