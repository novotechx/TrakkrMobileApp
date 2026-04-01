package com.novotech.trakkr.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TrakkrDarkScheme = darkColorScheme(
    primary = TrakkrColors.Gold,
    onPrimary = TrakkrColors.Background,
    primaryContainer = TrakkrColors.GoldDark,
    onPrimaryContainer = TrakkrColors.TextPrimary,
    secondary = TrakkrColors.GoldLight,
    onSecondary = TrakkrColors.Background,
    background = TrakkrColors.Background,
    onBackground = TrakkrColors.TextPrimary,
    surface = TrakkrColors.Surface,
    onSurface = TrakkrColors.TextPrimary,
    surfaceVariant = TrakkrColors.SurfaceContainer,
    onSurfaceVariant = TrakkrColors.TextDim,
    error = TrakkrColors.Error,
    onError = TrakkrColors.Background,
    outline = TrakkrColors.Outline,
    outlineVariant = TrakkrColors.Outline,
)

@Composable
fun TrakkrTheme(content: @Composable () -> Unit)
{
    MaterialTheme(
        colorScheme = TrakkrDarkScheme,
        typography = TrakkrTypography,
        content = content,
    )
}
