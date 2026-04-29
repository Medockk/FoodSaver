package com.foodsaver.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.foodsaver.app.ui.provider.LocalAppLocale

val LocalAppColorScheme: ProvidableCompositionLocal<ColorScheme> = staticCompositionLocalOf {
    lightColorScheme
}

val LocalTypography: ProvidableCompositionLocal<ThemeTypography>
    get() = staticCompositionLocalOf { ThemeTypography() }

@Composable
fun LocalFoodSaverThemeComposition(
    colorScheme: ColorScheme = FoodSaverTheme.colorScheme,
    typography: ThemeTypography = FoodSaverTheme.typography,
    locale: String = LocalAppLocale.current,
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalTypography provides typography
    ) {
        content()
    }
}

object FoodSaverTheme {

    val colorScheme: ColorScheme
        @Composable
        get() = LocalAppColorScheme.current

    val typography: ThemeTypography
        @Composable
        get() = Typography
}