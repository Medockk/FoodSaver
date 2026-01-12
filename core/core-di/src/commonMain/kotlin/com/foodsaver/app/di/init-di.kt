package com.foodsaver.app.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

internal expect val platformDiModule: Module

fun initKoinApp(
    modules: Array<Module>,
    koinAppDeclaration: KoinAppDeclaration? = null
) = startKoin {
        koinAppDeclaration?.invoke(this)
        modules(platformDiModule, *modules)
    }
