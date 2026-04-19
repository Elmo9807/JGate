package com.example.jgate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = JanusGold,
    onPrimary = TempleStone,
    primaryContainer = JanusGoldDark,
    onPrimaryContainer = Marble,
    secondary = Bronze,
    onSecondary = TempleStone,
    secondaryContainer = BronzeLight,
    onSecondaryContainer = TempleStone,
    background = TempleStone,
    onBackground = Marble,
    surface = TempleStoneMid,
    onSurface = Marble,
    surfaceVariant = TempleStoneLight,
    onSurfaceVariant = MarbleMuted,
    error = RomanRed,
    onError = Marble
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB8892E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFE0A0),
    onPrimaryContainer = Color(0xFF3D2E00),
    secondary = Color(0xFF8B5E3C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDCC2),
    onSecondaryContainer = Color(0xFF2E1500),
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    error = RomanRed,
    onError = Color(0xFFFFFFFF)
)

@Composable
fun JGateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}