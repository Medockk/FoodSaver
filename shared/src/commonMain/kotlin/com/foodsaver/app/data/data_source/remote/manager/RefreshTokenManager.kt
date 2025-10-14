package com.foodsaver.app.data.data_source.remote.manager

import com.foodsaver.app.data.data_source.remote.HttpConstants
import com.foodsaver.app.data.dto.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class RefreshTokenManager(
    private val client: HttpClient,
    private val tokenManager: TokenManager,
) {

    suspend fun refreshToken() {
        val refreshToken = tokenManager.getToken()
        if (refreshToken != null) {
            client.post("${HttpConstants.BASE_URL}auth/refresh") {
                val body = RefreshTokenRequest(refreshToken)
                setBody(body)
            }
        }
    }
}