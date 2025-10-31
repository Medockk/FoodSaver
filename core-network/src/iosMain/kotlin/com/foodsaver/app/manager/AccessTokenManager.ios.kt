package com.foodsaver.app.manager

import platform.Foundation.NSUserDefaults

actual class AccessTokenManager actual constructor() {

    private val storage = NSUserDefaults.standardUserDefaults
    private val refreshTokenKey = "REFRESH_TOKEN_KEY"
    private val jwtTokenKey = "JWT_TOKEN_KEY"

    actual suspend fun getRefreshToken(): String? {
        return storage.stringForKey(refreshTokenKey)
    }

    actual suspend fun setRefreshToken(refreshToken: String) {
        storage.setObject(refreshToken, refreshTokenKey)
    }

    actual suspend fun getJwtToken(): String? {
        return storage.stringForKey(jwtTokenKey)
    }

    actual suspend fun setJwtToken(jwtToken: String) {
    }

    actual suspend fun clearTokens() {
    }
}