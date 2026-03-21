package com.foodsaver.app.coreDb.di

import com.foodsaver.app.coreDb.data.factory.SqlDriverFactory
import com.foodsaver.app.coreDb.data.repository.DatabaseProviderImpl
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import org.koin.dsl.module

val databaseModule = module {

    single<SqlDriverFactory> {
        SqlDriverFactory()
    }
    single<DatabaseProvider> {
        DatabaseProviderImpl(get())
    }
}