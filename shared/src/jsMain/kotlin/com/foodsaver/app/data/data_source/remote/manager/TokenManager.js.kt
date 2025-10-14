package com.foodsaver.app.data.data_source.remote.manager

import kotlinx.browser.localStorage

actual class TokenManager {

    private val sp = localStorage
    private val key = "SHARED_JWT_TOKEN_PREFS"

    actual suspend fun getToken(): String? {
        return sp.getItem(key)
    }

    actual suspend fun setToken(token: String) {
        sp.setItem(key, token)
    }

    actual suspend fun clearStorage() {
        sp.clear()
    }
}