package com.foodsaver.app.data.data_source.remote.manager

import platform.Foundation.NSUserDefaults

actual class TokenManager {

    private val sp = NSUserDefaults.standardUserDefaults
    private val key = "SHARED_JWT_TOKEN_PREFS"

    actual suspend fun getToken(): String? {
        return sp.stringForKey(key)
    }

    actual suspend fun setToken(token: String) {
        sp.setObject(token, key)
    }

    actual suspend fun clearStorage() {
        sp.removeObjectForKey(key)
    }
}