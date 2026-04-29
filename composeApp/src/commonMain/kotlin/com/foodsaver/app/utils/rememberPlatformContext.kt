package com.foodsaver.app.utils

import androidx.compose.runtime.Composable
import com.foodsaver.app.commonModule.utils.PlatformContext

@Composable
expect fun rememberPlatformContext(): PlatformContext