package com.ma.sniffer.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ma.sniffer.R
import com.ma.sniffer.presentation.common.EmptyStatesFaces
import com.ma.sniffer.presentation.common.Message
import com.ma.sniffer.presentation.common.MessageAction
import com.ma.sniffer.presentation.feature.more.MoreScreen
import com.ma.sniffer.presentation.feature.usage.UsageScreen

@Composable
fun Navigation(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Speed) }

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        },
        entryProvider = entryProvider {
            entry<Screen.Speed> {
                UsageScreen(
                    onMoreClick = {
                        backStack.add(Screen.More)
                    }
                )
            }

            entry<Screen.More> {
                MoreScreen(
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }
        }
    )

    if (!hasPermission) {
        Message(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            message = stringResource(R.string.notification_permission_message),
            messageStyle = MaterialTheme.typography.bodyMedium,
            faces = EmptyStatesFaces.suggestion,
            action = MessageAction(
                name = stringResource(R.string.notification_permission_grant),
                action = onRequestPermission
            )
        )
    }
}