package com.foodsaver.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColorScheme =  lightColorScheme(
    primary = Color(0xFF472C9C),
    onPrimary = Color(0xffffffff),

    inversePrimary = Color(0xFF898EBC),

    primaryFixed = Color(0xFF061737),
    onPrimaryFixed = Color.White,
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
    // Основные цвета
    // Primary (472C9C) становится ярче (Tone 80)
    primary = Color(0xFFC9CFFF),
    // Цвет текста на Primary (белый) становится темным (Tone 20)
    onPrimary = Color(0xFF131343),

    // Второстепенные цвета
    secondary = Color(0xFFC7C7D0),
    // Цвет текста на Secondary становится темным
    onSecondary = Color(0xFF33333F),

    // Светлый контейнер (F0F5FA) становится темнее
    secondaryContainer = Color(0xFF282B3E),
    // Цвет текста на SecondaryContainer становится светлее
    onSecondaryContainer = Color(0xFFE3E5FF),

    // Цвета поверхностей
    // Background инвертируется в очень темный (Tone 10)
    background = Color(0xFF141414),
    // OnBackground инвертируется в светлый (Tone 90)
    onBackground = Color(0xFFE6E6E6),

    // Surface, SurfaceDim также темные
    surface = Color(0xFF1F1F1F),
    surfaceDim = Color(0xFF3B3B3B), // Ваш secondaryFixedDim хорошо подходит для Surface
    onSurface = Color(0xFFE6E6E6),

    // Разделители и обводки
    // Outline (F6F6F6) инвертируется в более темный
    outline = Color(0xFF4C4C4C),
    // OutlineVariant (3B3B3B) инвертируется в светлый
    outlineVariant = Color(0xFFBCBCBC),

    // Ошибки
    // Error (FF0000) становится ярче для темного фона
    error = Color(0xFFFFB4AB),
    // ErrorContainer (D61355) становится темнее
    errorContainer = Color(0xFF8C0000),

    // Дополнительные цвета
    inversePrimary = Color(0xFF5B45A8), // Инвертируем на тон primary

    // Fixed цвета, обычно не инвертируются, но меняют тон
    primaryFixed = Color(0xFF2D1875),
    secondaryFixed = Color(0xFF4C4C4C),
    secondaryFixedDim = Color(0xFF8F8F8F),
)

@Composable
fun colorScheme(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme()
): ColorScheme {

    return if (isSystemInDarkTheme) darkColorScheme
    else lightColorScheme
}