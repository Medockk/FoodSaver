package com.foodsaver.app.data.data_source.remote

import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClientFactory(
    private val baseUrl: String
) {

    fun create(): HttpClient {
        return createHttpClient {

            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json)

            }

            install(Logging)
            install(HttpCookies)
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    prettyPrint = true
                })
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