package com.foodsaver.app.data.data_source.remote

import com.foodsaver.app.data.data_source.remote.interceptor.AuthInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class KtorClientFactory(
    private val cookiesStorage: CookiesStorage,
    private val json: Json,
) {

    fun createMainClient(interceptor: AuthInterceptor): HttpClient {
        val client = with(interceptor) {
            createHttpClient {
                defaultRequest {
                    contentType(ContentType.Application.Json)
                }

                install(Logging)
                install(HttpCookies) {
                    storage = cookiesStorage
                }
                install(ContentNegotiation) {
                    json(json)
                }
                install(HttpRequestRetry) {
                    maxRetries = 3
                    retryOnExceptionIf { request, cause ->
                        cause is HttpRequestTimeoutException || cause is ConnectTimeoutException
                    }
                    delayMillis { it * 1500L }
                }
                install(HttpSend)
            }.withAuth()
        }

        return client
    }

    fun createBasicClient(): HttpClient {
        return createHttpClient {
            defaultRequest {
                url(HttpConstants.BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(Logging)
            install(HttpCookies) {
                storage = cookiesStorage
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnExceptionIf { request, cause ->
                    cause is HttpRequestTimeoutException || cause is ConnectTimeoutException
                }
                delayMillis { it * 1500L }
            }
        }
    }
}