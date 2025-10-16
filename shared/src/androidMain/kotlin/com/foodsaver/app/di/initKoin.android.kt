package com.foodsaver.app.di

import android.app.Application
import com.foodsaver.app.data.data_source.remote.manager.TokenManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<TokenManager> { TokenManager(get()) }
    }

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)

            modules(platformModule)
        }
    }
}