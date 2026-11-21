package com.ranjan.somiq.chat.ui.conversation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.chat.ui.conversation.ConversationContract.Effect
import com.ranjan.somiq.core.presentation.util.collectEffects
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ConversationScreenHost(
    otherUserId: String,
    otherUserName: String,
    onStartVoiceCall: (String) -> Unit = {},
    onStartVideoCall: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: ConversationViewModel = koinViewModel(parameters = { parametersOf(otherUserId, otherUserName) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects { effect ->
        when (effect) {
            is Effect.StartVoiceCall -> onStartVoiceCall(effect.userId)
            is Effect.StartVideoCall -> onStartVideoCall(effect.userId)
        }
    }
    ConversationScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        modifier = modifier
    )
}
