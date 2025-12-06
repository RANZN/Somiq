package com.ranjan.somiq.core.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImportContacts
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomOutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentPadding : PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
    onClick: OnClick,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Unspecified
        ),
        contentPadding = contentPadding
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 10.dp),
                tint = Color.Unspecified
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
            modifier = Modifier.weight(1f),
            color = Color.Black
        )
    }
}


@Preview
@Composable
private fun CustomOutlinedButtonPrev() {
    CustomOutlinedButton(
        text = "Login",
        icon = Icons.Outlined.ImportContacts,
        onClick = {}
    )
}