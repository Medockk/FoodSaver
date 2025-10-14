package com.foodsaver.app.data.data_source.remote.manager

expect class TokenManager {

    suspend fun getToken(): String?
    suspend fun setToken(token: String)
    suspend fun clearStorage()
}