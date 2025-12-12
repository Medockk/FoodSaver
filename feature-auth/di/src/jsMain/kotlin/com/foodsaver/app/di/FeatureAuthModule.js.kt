package com.foodsaver.app.di

import com.foodsaver.app.data.repository.GoogleAuthenticator
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module
    get() = module {
        single {
            GoogleAuthenticator()
        }
    }