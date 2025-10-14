package com.foodsaver.app.data.data_source.remote.manager

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class TokenManager() {

    suspend fun getToken(): String?
    suspend fun setToken(token: String)
    suspend fun clearStorage()
}