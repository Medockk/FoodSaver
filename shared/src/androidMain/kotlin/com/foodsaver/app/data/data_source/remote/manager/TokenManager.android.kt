package com.foodsaver.app.data.data_source.remote.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

actual class TokenManager actual constructor(){

    /**
     * Constructor for android platform
     */
    constructor(context: Context) : this() {
        this.sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    private val key = "SHARED_JWT_TOKEN_PREFS"
    private lateinit var sp: SharedPreferences

    actual suspend fun getToken(): String? {
        return sp.getString(key, null)
    }

    actual suspend fun setToken(token: String) {
        sp.edit { remove(key).putString(key, token) }
    }

    actual suspend fun clearStorage() {
        sp.edit { clear() }
    }
}