package com.foodsaver.app.client

import com.foodsaver.app.manager.AuthInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.sse.SSE
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

internal class HttpClientFactory(
    private val json: Json,
    private val cookiesStorage: CookiesStorage,
) {

    internal fun createMainHttpClient(authInterceptor: AuthInterceptor): HttpClient {
        val client = with(authInterceptor) {
            createHttpClient {

                defaultRequest {
                    contentType(ContentType.Application.Json)
                }
                install(ContentNegotiation) {
                    json(json)
                }
                install(Logging) {
                    level = LogLevel.HEADERS
                    logger = Logger.DEFAULT

                    filter { request ->
                        val isFileSending = request.headers[HttpHeaders.ContentType]?.contains("image") == true
                        if (isFileSending) {
                            level = LogLevel.NONE
                        }

                        true
                    }
                }
                install(HttpCookies) {
                    storage = cookiesStorage
                }
                install(HttpSend)
                install(HttpRequestRetry) {
                    maxRetries = 3
                    delayMillis { it * 3_000L }
                    retryOnException(3, true)
//                    retryOnExceptionIf(maxRetries = 3) { _, cause ->
//                        cause !is ConnectTimeoutException
//                    }
                }

                install(SSE) {
                    this.maxReconnectionAttempts = 4
                    this.reconnectionTime = 5.seconds
                    this.showCommentEvents()
                    this.showRetryEvents()
                }
            }.intercept()
        }

        return client
    }
}