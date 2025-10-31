package com.foodsaver.app.manager

import com.foodsaver.app.dto.RefreshRequestDto
import com.foodsaver.app.dto.RefreshResponseDto
import com.foodsaver.app.utils.HttpConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url

internal class AuthInterceptor(
    private val accessTokenManager: AccessTokenManager,
    private val cookiesStorage: CookiesStorage
) {

    internal fun HttpClient.intercept(): HttpClient {
        plugin(HttpSend).intercept { request ->

            if (cookiesStorage.get(Url(HttpConstants.BASE_URL)).isEmpty()) {
                accessTokenManager.getJwtToken()?.let { jwtToken ->
                    request.header(HttpHeaders.Authorization, "Bearer $jwtToken")
                }
            }

            val response = execute(request)

            if (response.response.status == HttpStatusCode.Unauthorized) {
                println("Intercept unauthorized exception...\nTry to refresh jwt token")

                val refreshToken = accessTokenManager.getRefreshToken()
                if (refreshToken != null) {
                    val refreshRequestDto = RefreshRequestDto(refreshToken)
                    val refreshResponse = post("${HttpConstants.BASE_URL}${HttpConstants.REFRESH}") {
                        setBody(refreshRequestDto)
                    }.body<RefreshResponseDto>()

                    accessTokenManager.setJwtToken(refreshResponse.jwtToken)

                    return@intercept execute(request)
                }
            }

            return@intercept response
        }
        return this
    }
}