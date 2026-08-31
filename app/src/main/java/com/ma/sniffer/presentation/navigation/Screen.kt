package com.ma.sniffer.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Speed : Screen

    @Serializable
    data object More : Screen
}