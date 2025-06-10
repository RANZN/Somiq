package com.ranjan.smartcents.quiz.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranjan.smartcents.component.OnClick

@Composable
fun OptionButton(
    text: String,
    optionNumber: Int,
    modifier: Modifier = Modifier,
    isSelect: Boolean = false,
    onClick: OnClick
) {
    val optionText = when (optionNumber + 1) {
        1 -> "A"
        2 -> "B"
        3 -> "C"
        4 -> "D"
        else -> ""
    }
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelect) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent,
            contentColor = Color.Black,
        )
    ) {
        Text(optionText, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}