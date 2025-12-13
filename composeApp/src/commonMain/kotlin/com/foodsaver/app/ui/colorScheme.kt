package com.foodsaver.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColorScheme = lightColorScheme(
    primary = Color(0xFF472C9C),
    onPrimary = Color(0xffffffff),

    inversePrimary = Color(0xFF898EBC),

    primaryFixed = Color(0xFF061737),
    primaryFixedDim = Color(0xFFEDEFFF),

    outline = Color(0xFFF6F6F6),
    outlineVariant = Color(0xFF3B3B3B),

    background = Color(0xffffffff),
    onBackground = Color(0xFF000000),

    surfaceDim = Color(0xFF9D9D9D),
    secondaryFixedDim = Color(0xFF3B3B3B),

    error = Color(0xFFFF0000),
    errorContainer = Color(0xFFD61355),

    secondaryContainer = Color(0xFFF0F5FA),
    onSecondaryContainer = Color(0xFF6B6E82),

    secondaryFixed = Color(0xFFD2D4D8),
    secondary = Color(0xFFF4F4F4),
    onSecondary = Color(0x80000000),
)
private val darkColorScheme = darkColorScheme(
    primary = Color(0xFF472C9C),
    onPrimary = Color(0xffffffff),

    outline = Color(0xFFF6F6F6),
    outlineVariant = Color(0xFF3B3B3B),

    background = Color(0xffffffff),
    onBackground = Color(0xFF000000),

    error = Color(0xFFFF0000),
    errorContainer = Color(0xFFD61355),

    secondaryContainer = Color(0xFFF0F5FA),
    onSecondaryContainer = Color(0xFF6B6E82),

    secondaryFixed = Color(0xFFD2D4D8),
    secondary = Color(0xFFF4F4F4)
)

@Composable
fun colorScheme(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme()
): ColorScheme {

    return if (isSystemInDarkTheme) darkColorScheme
    else lightColorScheme
}