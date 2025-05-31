package com.ranjan.smartcents.android.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ranjan.smartcents.android.R

@Composable
fun SignupHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.app_name_txt),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp)
        )
        Text(
            text = stringResource(R.string.create_your_account),
            style = MaterialTheme.typography.headlineMedium
        )
    }

}

@Preview
@Composable
private fun SignupHeaderPrev() {
    SignupHeader()
}