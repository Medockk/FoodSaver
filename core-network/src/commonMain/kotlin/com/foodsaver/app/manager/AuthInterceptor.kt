@file:OptIn(InternalAPI::class)

package com.foodsaver.app.manager

import com.foodsaver.app.dto.RefreshRequestDto
import com.foodsaver.app.dto.RefreshResponseDto
import com.foodsaver.app.utils.HttpConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.plugins.pluginOrNull
import io.ktor.client.request.cookie
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class AuthInterceptor(
    private val accessTokenManager: AccessTokenManager,
    private val cookiesStorage: CookiesStorage,
) {

    private val mutex = Mutex()
    private val bearerHeader = "Bearer"

    internal fun HttpClient.intercept(): HttpClient {
        pluginOrNull(HttpSend)?.intercept { originalRequest ->

            // setup JWT-token before executing original request
            accessTokenManager.getJwtToken()?.let { localJwtToken ->
                originalRequest.header(HttpHeaders.Authorization, "$bearerHeader $localJwtToken")
                println("Jwt token append in headers of original request")
            }

            // setup CSRF-token before executing original request
            accessTokenManager.getCsrfToken()?.let { csrf ->
                cookiesStorage.addCookie(originalRequest.url.build(), Cookie(
                    name = HttpConstants.CSRF_COOKIE_NAME,
                    value = csrf,
                    path = "/"
                ))
                originalRequest.header(HttpConstants.CSRF_HEADER_NAME, csrf)
                println("CSRF token append")
            }

            val originalResponse = execute(originalRequest)

            // If jwt token expires or original request does not have jwt token
            if (originalResponse.response.status == HttpStatusCode.Unauthorized) {
                mutex.withLock {
                    val currentLocalJwtToken = accessTokenManager.getJwtToken()
                    val currentRequestJwtToken =
                        originalRequest.headers[HttpHeaders.Authorization]?.removePrefix("$bearerHeader ")

                    // If other thread do refresh request
                    if (currentLocalJwtToken != currentRequestJwtToken && currentLocalJwtToken != null) {
                        println("Other thread already execute refresh request")
                        originalRequest.header(
                            HttpHeaders.Authorization,
                            "$bearerHeader $currentLocalJwtToken"
                        )
                        return@intercept execute(originalRequest)
                    }

                    println("Intercept unauthorized exception")

                    try {
                        val currentRefreshToken = accessTokenManager.getRefreshToken()
                            ?: return@intercept originalResponse
                        val refreshRequestDto = RefreshRequestDto(
                            refreshToken = currentRefreshToken,
                            accessToken = currentLocalJwtToken
                        )

                        val refreshResponse = post(HttpConstants.REFRESH_URL) {
                            setBody(refreshRequestDto)
                            expectSuccess = false
                        }

                        if (refreshResponse.status.isSuccess()) {
                            val refreshResponseDto = refreshResponse.body<RefreshResponseDto>()
                            println("Success refresh request $refreshResponseDto")

                            val newJwtToken = refreshResponseDto.jwtToken
                            accessTokenManager.setJwtToken(newJwtToken)

                            originalRequest.header(
                                HttpHeaders.Authorization,
                                "$bearerHeader $newJwtToken"
                            )
                            return@intercept execute(originalRequest)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        return@intercept originalResponse
                    }
                }
            }

            // If original request does not have CSRF-token in headers AND in cookies
            if (originalResponse.response.status == HttpStatusCode.Forbidden) {
                val localCsrfToken = accessTokenManager.getCsrfToken()
                    ?: return@intercept originalResponse

                originalRequest.header(HttpConstants.CSRF_HEADER_NAME, localCsrfToken)
                originalRequest.cookie(HttpConstants.CSRF_COOKIE_NAME, localCsrfToken)

                return@intercept execute(originalRequest)
            }

            cookiesStorage.addTokenToLocalStorage(
                tokenName = HttpConstants.CSRF_COOKIE_NAME,
                onSave = { csrf ->
                    accessTokenManager.setCsrfToken(csrf)
                }
            )

            return@intercept originalResponse
        }
        return this
    }

    /**
     * Method to find from [CookiesStorage] the [Cookie] with special [tokenName]
     * @param tokenName The cookie-name
     * @return The value of [Cookie] with name [tokenName]
     */
    private suspend fun CookiesStorage.getTokenFromCookies(tokenName: String): String? {
        val token = this.get(Url(HttpConstants.ROOT_URL))
            .find { it.name == tokenName }
        return token?.value
    }

    /**
     * Method to save token to local storage
     * @param tokenName The name of [Cookie] to exact value
     * @param onSave lambda to save token to local storage
     */
    private suspend fun CookiesStorage.addTokenToLocalStorage(
        tokenName: String,
        onSave: suspend (token: String) -> Unit,
    ) {
        val token = getTokenFromCookies(tokenName) ?: return
        onSave(token)
    }
}
