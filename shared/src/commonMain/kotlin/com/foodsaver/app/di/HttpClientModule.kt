package com.foodsaver.app.di

import com.foodsaver.app.data.data_source.remote.KtorClientFactory
import com.foodsaver.app.data.data_source.remote.interceptor.AuthInterceptor
import com.foodsaver.app.data.data_source.remote.manager.RefreshTokenManager
import com.foodsaver.app.data.data_source.remote.manager.TokenManager
import com.foodsaver.app.di.utils.HttpClientType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val httpClientModule = module {

    single<CookiesStorage> { AcceptAllCookiesStorage() }
    single<TokenManager> { TokenManager() }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
        }
    }

    single<KtorClientFactory> { KtorClientFactory(get(), get()) }
    factory<HttpClient>(named(HttpClientType.BASIC_CLIENT)) {
        get<KtorClientFactory>().createBasicClient()
    }

    single<RefreshTokenManager> {
        RefreshTokenManager(
            client = get<HttpClient>(named(HttpClientType.BASIC_CLIENT)),
            tokenManager = get()
        )
    }
    single<AuthInterceptor> {
        AuthInterceptor(get<RefreshTokenManager>())
    }
    single<HttpClient>(named(HttpClientType.MAIN_CLIENT)) {
        get<KtorClientFactory>().createMainClient(get<AuthInterceptor>())
    }
}