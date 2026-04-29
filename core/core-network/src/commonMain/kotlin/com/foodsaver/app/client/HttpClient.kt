package com.foodsaver.app.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

internal expect fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient