package com.foodsaver.app.coreAuth

import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val coreAuthPlatformModule: Module
    get() = module {
        single<AuthUserManager> {
            AuthUserManager(get())
        }
    }