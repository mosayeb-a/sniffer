package com.ma.sniffer.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = OffWhite,
    onPrimaryContainer = Black,
    secondary = SoftPink,
    onSecondary = Black,
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

@Composable
fun SnifferTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}