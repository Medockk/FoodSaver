package com.foodsaver.app.di

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module

lateinit var applicationContext: Context
actual val platformDiModule: Module
    get() = module {
        single<Context>(createdAtStart = true) { applicationContext }
    }