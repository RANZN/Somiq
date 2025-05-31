package com.ranjan.smartcents.android.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.ranjan.smartcents.android.R

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.app_name_txt),
        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 64.sp),
        modifier = modifier
    )
}