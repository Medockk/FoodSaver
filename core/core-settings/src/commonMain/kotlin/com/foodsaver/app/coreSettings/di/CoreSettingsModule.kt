package com.foodsaver.app.coreSettings.di

import com.foodsaver.app.coreSettings.data.LocaleRepositoryImpl
import com.foodsaver.app.coreSettings.data.provider.DefaultLocaleProviderImpl
import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider
import com.foodsaver.app.coreSettings.domain.repository.LocaleRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformCoreSettingsModule: Module
val coreSettingsModule = module {
    includes(platformCoreSettingsModule)

    single<DefaultLocaleProvider> {
        DefaultLocaleProviderImpl()
    }

    single<LocaleRepository> {
        LocaleRepositoryImpl(get())
    }
}