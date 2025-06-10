package com.ranjan.smartcents.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import smartcents.composeapp.generated.resources.Res
import com.ranjan.smartcents.component.FillSpacer
import com.ranjan.smartcents.quiz.components.OptionButton
import com.ranjan.smartcents.util.defaultPadding
import com.ranjan.smartcents.presentation.quiz.QuizViewModel
import kotlinx.coroutines.launch
import smartcents.composeapp.generated.resources.*

@Composable
fun QuizScreen(
    uiState: QuizViewModel.UiState,
    action: (QuizViewModel.Action) -> Unit
) {
    val scope = rememberCoroutineScope()
    if (uiState.isLoading) {
        CircularProgressIndicator()
        return
    }

    val pagerState = rememberPagerState(initialPage = 0) { uiState.questions.size }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.defaultPadding()) {
            Text(
                stringResource(Res.string.finmaster_quiz),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(vertical = 20.dp),
            )
            Row(Modifier.fillMaxWidth()) {
                Text(
                    stringResource(
                        Res.string.question,
                        pagerState.currentPage + 1,
                        uiState.questions.size
                    ),
                )
                FillSpacer()
                Text(stringResource(Res.string.time_left, uiState.time))
            }
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                var selectedIndex by remember { mutableIntStateOf(-1) }
                Column(Modifier.fillMaxSize()) {
                    val question = uiState.questions[page]
                    Text(
                        question.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    LazyColumn(userScrollEnabled = false) {
                        itemsIndexed(question.options, key = { pos, item -> item }) { pos, text ->
                            OptionButton(
                                text = text,
                                optionNumber = pos,
                                isSelect = pos == selectedIndex,
                                onClick = { selectedIndex = pos }
                            )
                        }
                    }
                    FillSpacer()
                    val isLastQuestion = page == uiState.questions.size - 1
                    Button(
                        onClick = {
                            scope.launch {
                                action(
                                    QuizViewModel.Action.SaveAnswer(
                                        questionId = page,
                                        answer = question.options[selectedIndex],
                                        correctAnswer = question.correctOption,
                                    )
                                )

                                if (isLastQuestion) {
                                    action(QuizViewModel.Action.NavigateToQuizResult)
                                    return@launch
                                }
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        val text = if (isLastQuestion) {
                            stringResource(Res.string.submit)
                        } else {
                            stringResource(Res.string.next)
                        }
                        Text(text)
                    }
                }
            }
        }
    }
}