package com.foodsaver.app.coreLocation.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformCoreLocationModule: Module
val coreLocationModule = module {
    includes(platformCoreLocationModule)
}