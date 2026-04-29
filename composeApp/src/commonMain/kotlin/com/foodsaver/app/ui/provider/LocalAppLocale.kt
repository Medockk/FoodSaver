package com.foodsaver.app.ui.provider

import androidx.compose.runtime.Composable

expect object LocalAppLocale {

    val current: String
        @Composable get
}