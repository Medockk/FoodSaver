package com.foodsaver.app.di

import com.foodsaver.app.client.HttpClientFactory
import com.foodsaver.app.client.HttpClientType
import com.foodsaver.app.manager.AccessTokenManager
import com.foodsaver.app.manager.AuthInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
        }
    }

    single<CookiesStorage> {
        AcceptAllCookiesStorage()
    }

    single<HttpClientFactory> {
        HttpClientFactory(
            json = get(),
            cookiesStorage = get()
        )
    }

    single {
        AccessTokenManager()
    }
    single<AuthInterceptor> {
        AuthInterceptor(
            accessTokenManager = get(),
            cookiesStorage = get()
        )
    }

    single<HttpClient>(named(HttpClientType.MAIN_CLIENT)) {
        get<HttpClientFactory>().createMainHttpClient(
            authInterceptor = get()
        )
    }
}