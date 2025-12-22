package com.foodsaver.app.di

import com.foodsaver.app.utils.IsUserAuthenticated
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformUiModule: Module
    get() = module {
        single {
            IsUserAuthenticated()
        }
    }