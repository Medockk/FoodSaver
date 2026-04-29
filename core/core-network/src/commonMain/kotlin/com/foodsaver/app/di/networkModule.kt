package com.foodsaver.app.di

import com.foodsaver.app.client.HttpClientFactory
import com.foodsaver.app.manager.AccessTokenManager
import com.foodsaver.app.manager.AuthInterceptor
import com.foodsaver.app.manager.CsrfTokenManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.serialization.json.Json
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

    single<CsrfTokenManager> {
        CsrfTokenManager()
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
            cookiesStorage = get(),
            csrfTokenManager = get()
        )
    }

    single<HttpClient> {
        get<HttpClientFactory>().createMainHttpClient(
            authInterceptor = get()
        )
    }
}