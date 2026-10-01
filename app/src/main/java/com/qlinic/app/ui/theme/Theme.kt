package com.qlinic.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QlinicColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueDark,
    secondary = PinkPrimary,
    onSecondary = White,
    secondaryContainer = PinkLight,
    onSecondaryContainer = PinkDark,
    tertiary = YellowAccent,
    onTertiary = TextPrimary,
    background = BackgroundGray,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundGray,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    error = RedError,
    onError = White,
)

@Composable
fun QlinicTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QlinicColorScheme,
        typography = QlinicTypography,
        content = content
    )
}
