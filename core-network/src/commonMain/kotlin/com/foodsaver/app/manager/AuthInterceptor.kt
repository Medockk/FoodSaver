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

            val jwt = cookiesStorage.getTokenFromCookies("jwt")

            if (jwt == null) {
                accessTokenManager.getJwtToken()?.let { jwtToken ->
                    request.header(HttpHeaders.Authorization, "Bearer $jwtToken")
                }
            }

            val csrf = cookiesStorage.getTokenFromCookies(HttpConstants.CSRF_COOKIE_NAME)

            if (csrf == null) {
                accessTokenManager.getCsrfToken()?.let { csrfToken ->
                    request.header(HttpConstants.CSRF_HEADER_NAME, csrfToken)
                }
            }

            val response = execute(request)

            if (response.response.status == HttpStatusCode.Unauthorized) {
                println("Intercept unauthorized exception...\nTry to refresh jwt token")

                val refreshToken = accessTokenManager.getRefreshToken()
                val jwtToken = accessTokenManager.getJwtToken()

                if (refreshToken != null) {
                    val refreshRequestDto = RefreshRequestDto(refreshToken, jwtToken)
                    val refreshResponse = post("${HttpConstants.BASE_URL}${HttpConstants.REFRESH}") {
                        setBody(refreshRequestDto)
                    }.body<RefreshResponseDto>()

                    accessTokenManager.setJwtToken(refreshResponse.jwtToken)
                    cookiesStorage.addTokenToLocalStorage(
                        tokenName = HttpConstants.CSRF_COOKIE_NAME,
                        onSave = { csrfToken ->
                            accessTokenManager.setCsrfToken(csrfToken)
                        }
                    )

                    return@intercept execute(request)
                }
            }
            cookiesStorage.addTokenToLocalStorage(
                tokenName = HttpConstants.CSRF_COOKIE_NAME,
                onSave = { csrfToken ->
                    accessTokenManager.setCsrfToken(csrfToken)
                }
            )

            return@intercept response
        }
        return this
    }
    private suspend fun CookiesStorage.getTokenFromCookies(tokenName: String): String? {
        val csrfToken = this.get(Url(HttpConstants.ROOT_URL))
            .find { it.name == tokenName }
        return csrfToken?.value
    }
    private suspend fun CookiesStorage.addTokenToLocalStorage(tokenName: String, onSave: suspend (token: String) -> Unit) {
        val token = getTokenFromCookies(tokenName) ?: return
        onSave(token)
    }
}
