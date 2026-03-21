package com.foodsaver.app.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

actual class AccessTokenManager actual constructor() {

    private var sp: SharedPreferences
    private val refreshTokenKey = "REFRESH_TOKEN_KEY"
    private val jwtTokenKey = "JWT_TOKEN_KEY"
    private val csrfTokenKey = "CSRF_TOKEN_KEY"

    init {
        val context by inject<Context>(Context::class.java)
        this.sp = try {
            createSharedPreferences(context)
        } catch (_: Exception) {
            context.deleteSharedPreferences("secure_access_tokens")
            createSharedPreferences(context)
        }
    }

    private fun createSharedPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            "secure_access_tokens",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    actual suspend fun getRefreshToken(): String? {
        return withContext(Dispatchers.IO) { sp.getString(refreshTokenKey, null) }
    }

    actual suspend fun setRefreshToken(refreshToken: String) = withContext(Dispatchers.IO){
        sp.edit { remove(refreshTokenKey).putString(refreshTokenKey, refreshToken) }
    }

    actual suspend fun getJwtToken(): String? = withContext(Dispatchers.IO) {
        sp.getString(jwtTokenKey, null)
    }

    actual suspend fun setJwtToken(jwtToken: String) = withContext(Dispatchers.IO) {
        sp.edit { remove(jwtTokenKey).putString(jwtTokenKey, jwtToken) }
    }

    actual suspend fun clearTokens()= withContext(Dispatchers.IO) {
        sp.edit { clear() }
    }

    actual suspend fun getCsrfToken(): String? = withContext(Dispatchers.IO) {
        sp.getString(csrfTokenKey, null)
    }

    actual suspend fun setCsrfToken(csrfToken: String) = withContext(Dispatchers.IO)  {
        sp.edit { remove(csrfTokenKey).putString(csrfTokenKey, csrfToken) }
    }
}