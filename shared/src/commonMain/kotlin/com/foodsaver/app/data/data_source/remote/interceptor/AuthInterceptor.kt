package com.foodsaver.app.data.data_source.remote.interceptor

import com.foodsaver.app.data.data_source.remote.manager.TokenManager
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.cookie
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class AuthInterceptor(
    private val tokenManager: TokenManager
) {

    suspend fun intercept(request: HttpRequestBuilder) = createClientPlugin("JWTTokenPlugin") {
        onResponse { response ->
            if (response.status.value == HttpStatusCode.Unauthorized.value) {
                tokenManager.getToken()?.let { refreshToken ->

                }
            }
        }
    }
}