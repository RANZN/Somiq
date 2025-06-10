package com.ranjan.smartcents.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.sp
import smartcents.composeapp.generated.resources.Res
import smartcents.composeapp.generated.resources.app_name_txt

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(Res.string.app_name_txt),
        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 64.sp),
        modifier = modifier
    )
}