package com.foodsaver.app.coreSettings.di

import com.foodsaver.app.coreSettings.data.factory.DataStoreFactoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformCoreSettingsModule: Module
    get() = module {
        single<com.foodsaver.app.coreSettings.data.factory.DataStoreFactory> {
            DataStoreFactoryImpl()
        }
    }