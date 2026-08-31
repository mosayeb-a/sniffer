package com.ma.sniffer.presentation.common

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ma.sniffer.R

@Composable
fun ControlButton(
    isRunning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (containerColor, contentColor) = if (isRunning) {
        MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
    }

    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        expanded = true,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp,
            pressedElevation = 3.dp
        ),
        icon = {
            AnimatedContent(
                targetState = isRunning,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(300)) +
                            scaleIn(initialScale = 0.8f, animationSpec = tween(300))) togetherWith
                            (fadeOut(animationSpec = tween(300)) +
                                    scaleOut(targetScale = 0.8f, animationSpec = tween(300)))
                },
                label = "icon"
            ) { monitoring ->
                Icon(
                    imageVector = if (monitoring) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (monitoring) stringResource(R.string.stop_monitoring) else stringResource(R.string.start_monitoring),
                )
            }
        },
        text = {
            AnimatedContent(
                targetState = isRunning,
                transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { if (isRunning) -it else it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeIn(
                        animationSpec = tween(300)
                    ) togetherWith
                            slideOutVertically(
                                targetOffsetY = { if (isRunning) it else -it },
                                animationSpec = tween(300, easing = FastOutSlowInEasing)
                            ) + fadeOut(
                        animationSpec = tween(300)
                    )
                },
                label = "text"
            ) { monitoring ->
                Text(
                    text = if (monitoring) stringResource(R.string.stop) else stringResource(R.string.start),
                    fontSize = 16.sp
                )
            }
        }
    )
}