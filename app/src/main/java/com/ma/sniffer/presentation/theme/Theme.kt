package com.ma.sniffer.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = OffWhite,
    onPrimaryContainer = Black,
    secondary = SoftPink,
    onSecondary = White,
    secondaryContainer = LightPink,
    onSecondaryContainer = Black,
    background = OffWhiteBackground,
    onBackground = Black,
    surface = PureWhite,
    onSurface = Black,
    surfaceVariant = LightGrey,
    onSurfaceVariant = Grey,
    outline = LightOutline,
    outlineVariant = VeryLightOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = DarkSurface,
    onPrimaryContainer = White,
    secondary = SoftPink,
    onSecondary = Black,
    secondaryContainer = DarkPink,
    onSecondaryContainer = White,
    background = DarkBackground,
    onBackground = White,
    surface = DarkSurface,
    onSurface = White,
    surfaceVariant = DarkGrey,
    onSurfaceVariant = LightGrey,
    outline = DarkOutline,
    outlineVariant = VeryDarkOutline
)

@Composable
fun SnifferTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}