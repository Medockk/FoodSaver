package com.foodsaver.app.data.data_source.remote.manager

import android.content.Context
import androidx.core.content.edit

actual class TokenManager(
    private val context: Context
) {
    private val key = "SHARED_JWT_TOKEN_PREFS"
    private val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)

    actual suspend fun getToken(): String? {
        return sp.getString(key, null)
    }

    actual suspend fun setToken(token: String) {
        sp.edit { remove(key).putString(key, key) }
    }

    actual suspend fun clearStorage() {
        sp.edit { clear() }
    }
}