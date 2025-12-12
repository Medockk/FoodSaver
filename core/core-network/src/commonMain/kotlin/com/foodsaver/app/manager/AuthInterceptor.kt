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
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.coroutines.ensureActive
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
                originalRequest.replaceAuthorizationHeader(localJwtToken)
            }

            // setup CSRF-token before executing original request
            accessTokenManager.getCsrfToken()?.let { csrfToken ->
                originalRequest.replaceCsrfHeader(csrfToken)
            }

            val originalResponse = execute(originalRequest)

            // If jwt token expires or original request does not have jwt token
            if (originalResponse.response.status == HttpStatusCode.Unauthorized) {
                println("Response with Unauthorized status")
                mutex.withLock {
                    val currentLocalJwtToken = accessTokenManager.getJwtToken()

                    // If other thread do refresh request
                    if (originalRequest.isTokenAlreadyUpdated(currentLocalJwtToken)) {
                        try {
                            ensureActive()
                            println("Other thread already execute refresh request")
                            currentLocalJwtToken?.let {
                                originalRequest.replaceAuthorizationHeader(it)
                            }
                            mutex.tryUnlock()
                            return@intercept execute(originalRequest)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return@intercept originalResponse
                        }
                    }

                    println("Intercept unauthorized exception")

                    try {
                        val currentRefreshToken = accessTokenManager.getRefreshToken()
                            ?: return@withLock
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

                            val newJwtToken = refreshResponseDto.jwtToken
                            accessTokenManager.setJwtToken(newJwtToken)

                            originalRequest.replaceAuthorizationHeader(newJwtToken)
                            mutex.tryUnlock()
                            return@intercept execute(originalRequest)
                        }

                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                        return@intercept originalResponse
                    }
                }

                return@intercept originalResponse
            }

            // If original request does not have CSRF-token in headers AND in cookies
            if (originalResponse.response.status == HttpStatusCode.Forbidden) {
                val localCsrfToken = cookiesStorage.getTokenFromCookies(HttpConstants.CSRF_COOKIE_NAME)
                    ?: accessTokenManager.getCsrfToken()
                    ?: return@intercept originalResponse

                originalRequest.replaceCsrfHeader(localCsrfToken)

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

    private fun HttpRequestBuilder.isTokenAlreadyUpdated(localToken: String?): Boolean {
        val currentRequestToken = this.headers[HttpHeaders.Authorization]?.removePrefix("$bearerHeader ")

        return currentRequestToken != localToken && localToken != null
    }

    private fun HttpRequestBuilder.replaceAuthorizationHeader(token: String) {
        val headerName = HttpHeaders.Authorization
        if (this.headers.contains(headerName)) {
            this.headers.remove(headerName)
        }
        this.header(headerName, "$bearerHeader $token")
    }

    private suspend fun HttpRequestBuilder.replaceCsrfHeader(token: String) {
        val headerName = HttpConstants.CSRF_HEADER_NAME
        val cookie = Cookie(
            name = HttpConstants.CSRF_COOKIE_NAME,
            value = token,
            path = "/",
            domain = null,
            httpOnly = false
        )
        cookiesStorage.addCookie(this.url.build(), cookie)

        if (this.headers.contains(headerName)) {
            this.headers.remove(headerName)
        }

        this.header(headerName, token)
    }

    private fun Mutex.tryUnlock(owner: Any? = null) = try {
        unlock(owner)
    } catch (_: Exception) {

    }
}
