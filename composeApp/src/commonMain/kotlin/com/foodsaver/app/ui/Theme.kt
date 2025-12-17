package com.foodsaver.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalTypography
    get() = staticCompositionLocalOf { ThemeTypography() }


@Composable
fun Theme(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    themeTypography: ThemeTypography = Typography,
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalTypography provides themeTypography,
        content = content
    )
}