package com.foodsaver.app.coreAuth

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val coreAuthPlatformModule: Module

val coreAuthModule = module {
    includes(coreAuthPlatformModule)
}