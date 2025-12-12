package com.foodsaver.app.manager

import kotlinx.browser.localStorage

actual class AccessTokenManager actual constructor() {

    private val storage = localStorage

    private val refreshTokenKey = "REFRESH_TOKEN_KEY"
    private val jwtTokenKey = "JWT_TOKEN_KEY"
    private val csrfTokenKey = "CSRF_TOKEN_KEY"

    actual suspend fun getRefreshToken(): String? {
        return storage.getItem(refreshTokenKey)
    }

    actual suspend fun setRefreshToken(refreshToken: String) {
        storage.setItem(refreshTokenKey, refreshToken)
    }

    actual suspend fun getJwtToken(): String? {
        return storage.getItem(jwtTokenKey)
    }

    actual suspend fun setJwtToken(jwtToken: String) {
        storage.setItem(jwtTokenKey, jwtToken)
    }

    actual suspend fun clearTokens() {
        storage.clear()
    }

    actual suspend fun getCsrfToken(): String? {
        return storage.getItem(csrfTokenKey)
    }

    actual suspend fun setCsrfToken(csrfToken: String) {
        storage.setItem(csrfTokenKey, csrfToken)
    }
}