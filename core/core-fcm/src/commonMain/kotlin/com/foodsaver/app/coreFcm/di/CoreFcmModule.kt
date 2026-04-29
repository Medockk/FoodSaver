package com.foodsaver.app.coreFcm.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformCoreFcmModule: Module

val coreFcmModule = module {
    includes(platformCoreFcmModule)
}