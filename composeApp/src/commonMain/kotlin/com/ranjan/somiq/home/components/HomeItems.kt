package com.ranjan.somiq.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.component.OnClick
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeItems(
    iconRes: DrawableResource,
    text: String,
    modifier: Modifier = Modifier,
    onClick: OnClick
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.DarkGray),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(100.dp).padding(16.dp),
                tint = Color.Unspecified
            )
            Text(
                text = text,
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )
        }
    }
}