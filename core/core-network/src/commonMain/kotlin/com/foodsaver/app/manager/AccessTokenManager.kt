package com.foodsaver.app.manager

expect class AccessTokenManager() {

    suspend fun getRefreshToken(): String?
    suspend fun setRefreshToken(refreshToken: String)

    suspend fun getJwtToken(): String?
    suspend fun setJwtToken(jwtToken: String)

    suspend fun getCsrfToken(): String?
    suspend fun setCsrfToken(csrfToken: String)

    suspend fun clearTokens()
}
