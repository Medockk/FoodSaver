package com.foodsaver.app.data.data_source.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js

actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(Js) {
        config(this)
    }
}