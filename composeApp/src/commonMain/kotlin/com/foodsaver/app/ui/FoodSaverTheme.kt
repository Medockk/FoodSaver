package com.foodsaver.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.foodsaver.app.ui.provider.LocalAppLocale
import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig

val LocalAppColorScheme: ProvidableCompositionLocal<ColorScheme> = staticCompositionLocalOf {
    lightColorScheme
}

val LocalTypography: ProvidableCompositionLocal<ThemeTypography>
    get() = staticCompositionLocalOf { ThemeTypography() }

val LocalKamelConfig: KamelConfig = KamelConfig {
    takeFrom(KamelConfig.Default)
}

@Composable
fun LocalFoodSaverThemeComposition(
    colorScheme: ColorScheme = FoodSaverTheme.colorScheme,
    typography: ThemeTypography = FoodSaverTheme.typography,
    locale: String = LocalAppLocale.current,
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalTypography provides typography,
        LocalKamelConfig provides com.foodsaver.app.ui.LocalKamelConfig
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