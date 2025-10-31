package com.foodsaver.app.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import org.koin.java.KoinJavaComponent.inject

actual class AccessTokenManager actual constructor() {

    private var sp: SharedPreferences
    private val refreshTokenKey = "REFRESH_TOKEN_KEY"
    private val jwtTokenKey = "JWT_TOKEN_KEY"

    init {
        val context by inject<Context>(Context::class.java)
        this.sp = EncryptedSharedPreferences.create(
            "secure_access_tokens",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    actual suspend fun getRefreshToken(): String? {
        return sp.getString(refreshTokenKey, null)
    }

    actual suspend fun setRefreshToken(refreshToken: String) {
        sp.edit { remove(refreshTokenKey).putString(refreshTokenKey, refreshToken) }
    }

    actual suspend fun getJwtToken(): String? {
        return sp.getString(jwtTokenKey, null)
    }

    actual suspend fun setJwtToken(jwtToken: String) {
        sp.edit { remove(jwtTokenKey).putString(jwtTokenKey, jwtToken) }
    }

    actual suspend fun clearTokens() {
        sp.edit { clear() }
    }
}