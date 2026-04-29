package com.foodsaver.app.ui.provider

import androidx.compose.runtime.Composable
import java.util.Locale

actual object LocalAppLocale {

    private var defaultLocale: Locale? = null

    actual val current: String
        @Composable
        get() = Locale.getDefault().toString()
}