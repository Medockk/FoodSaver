@file:OptIn(InternalAPI::class)

package com.foodsaver.app.data.data_source.remote.interceptor

import com.foodsaver.app.data.data_source.remote.manager.RefreshTokenManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.InternalAPI

internal class AuthInterceptor(
    private val refreshTokenManager: RefreshTokenManager,
) {

    fun HttpClient.withAuth(): HttpClient {
        plugin(HttpSend).intercept { request ->
            val response = execute(request)

            if (response.response.status == HttpStatusCode.Unauthorized) {

                println("Intercept Unauthorize exception! Try to refresh token...")
                refreshTokenManager.refreshToken()
                return@intercept execute(request)
            }

            return@intercept response
        }
        return this
    }
}