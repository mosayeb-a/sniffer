package com.ma.sniffer.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ma.sniffer.presentation.feature.more.MoreScreen
import com.ma.sniffer.presentation.feature.usage.UsageScreen

@Composable
fun Navigation() {
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
}