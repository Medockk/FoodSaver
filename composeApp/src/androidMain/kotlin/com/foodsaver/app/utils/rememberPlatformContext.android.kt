package com.foodsaver.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.foodsaver.app.commonModule.utils.PlatformContext

@Composable
actual fun rememberPlatformContext(): PlatformContext {
    val context = LocalContext.current

    return remember(context) {
        PlatformContext(context)
    }
}