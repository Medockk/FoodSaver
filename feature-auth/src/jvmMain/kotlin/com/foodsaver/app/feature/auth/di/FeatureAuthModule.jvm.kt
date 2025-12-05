package com.foodsaver.app.feature.auth.di

import com.foodsaver.app.client.HttpClientType
import com.foodsaver.app.feature.auth.data.repository.AuthConnector
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformModule: Module
    get() = module {
        single<AuthConnector> {
            AuthConnector(get(named(HttpClientType.MAIN_CLIENT)))
        }
    }