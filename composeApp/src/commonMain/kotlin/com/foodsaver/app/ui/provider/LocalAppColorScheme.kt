package com.foodsaver.app.ui.provider

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

object LocalAppColorScheme {

    val current: ColorScheme
        @Composable
        get() = com.foodsaver.app.ui.FoodSaverTheme.colorScheme
}