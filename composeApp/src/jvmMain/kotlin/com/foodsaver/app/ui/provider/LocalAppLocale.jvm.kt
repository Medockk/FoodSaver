package com.foodsaver.app.ui.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalClipboard
import java.util.Locale

actual object LocalAppLocale {
    actual val current: String
        @Composable
        get() = Locale.getDefault().toString()
}