package com.foodsaver.app.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.foodsaver.app.ui.provider.LocalAppLocale

val LocalAppColorScheme: ProvidableCompositionLocal<ColorScheme> = staticCompositionLocalOf {
    lightColorScheme
}

@Composable
fun LocalFoodSaverThemeComposition(
    colorScheme: ColorScheme = FoodSaverTheme.colorScheme,
    locale: String = LocalAppLocale.current,
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme
    ) {
        content()
    }
}

object FoodSaverTheme {

    val colorScheme: ColorScheme
        @Composable
        get() = colorScheme()
}