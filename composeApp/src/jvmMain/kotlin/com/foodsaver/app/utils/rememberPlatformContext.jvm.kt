package com.foodsaver.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.foodsaver.app.commonModule.utils.PlatformContext

@Composable
actual fun rememberPlatformContext() = remember {
    PlatformContext()
}