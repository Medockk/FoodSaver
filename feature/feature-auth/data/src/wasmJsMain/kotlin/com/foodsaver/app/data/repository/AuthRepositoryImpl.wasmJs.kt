package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.utils.PlatformContext

actual class GoogleAuthenticator {
    internal actual suspend fun getGoogleIdToken(platformContext: PlatformContext): String? = null
}