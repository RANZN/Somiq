package com.ranjan.somiq.feed.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val DOUBLE_TAP_MS = 400L
private const val HEART_ANIM_MS = 700

@OptIn(ExperimentalTime::class)
@Composable
fun DoubleTapToLikeBox(
    modifier: Modifier = Modifier,
    onDoubleTap: () -> Unit,
    content: @Composable () -> Unit
) {
    var lastTapTime by remember { mutableLongStateOf(0L) }
    var heartTrigger by remember { mutableStateOf(0) }
    val showHeart = remember { Animatable(0f) }
    val heartScale = remember { Animatable(0.3f) }

    LaunchedEffect(heartTrigger) {
        if (heartTrigger > 0) {
            heartScale.snapTo(0.3f)
            showHeart.snapTo(1f)
            heartScale.animateTo(1.2f, animationSpec = tween(HEART_ANIM_MS / 2))
            showHeart.animateTo(0f, animationSpec = tween(HEART_ANIM_MS))
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        val now = Clock.System.now().toEpochMilliseconds()
                        if (now - lastTapTime < DOUBLE_TAP_MS) {
                            onDoubleTap()
                            heartTrigger++
                            lastTapTime = 0L
                        } else {
                            lastTapTime = now
                        }
                    }
                )
            }
    ) {
        content()

        if (showHeart.value > 0f) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp)
                    .scale(heartScale.value)
                    .alpha(showHeart.value),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
