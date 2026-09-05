package br.com.diogozarpelao.leiloesretrogames.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GamerPurple,
    onPrimary = GamerTextPrimary,

    secondary = GamerCyan,
    onSecondary = GamerBackground,

    tertiary = GamerBlue,

    background = GamerBackground,
    onBackground = GamerTextPrimary,

    surface = GamerSurface,
    onSurface = GamerTextPrimary,

    surfaceVariant = GamerSurfaceVariant,
    onSurfaceVariant = GamerTextSecondary,

    primaryContainer = GamerSurfaceVariant,
    onPrimaryContainer = GamerPurpleLight,

    secondaryContainer = GamerSurfaceVariant,
    onSecondaryContainer = GamerCyan,

    error = GamerRed
)

private val LightColorScheme = lightColorScheme(
    primary = GamerPurple,
    secondary = GamerBlue,
    tertiary = GamerCyan,

    background = GamerLightBackground,
    onBackground = GamerLightText,

    surface = GamerLightSurface,
    onSurface = GamerLightText,

    surfaceVariant = GamerLightSurfaceVariant,
    onSurfaceVariant = GamerLightText,

    error = GamerRed
)

@Composable
fun LeilõesRetroGamesTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}