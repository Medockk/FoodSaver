package com.foodsaver.app.data.data_source.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import java.util.concurrent.TimeUnit

actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(OkHttp) {
        config(this)

        engine {
            config {
                retryOnConnectionFailure(true)
                connectTimeout(15, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }
        }
    }
}