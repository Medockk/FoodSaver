package com.foodsaver.app.di

import com.foodsaver.app.data.factory.SqlDriverFactory
import com.foodsaver.app.data.repository.DatabaseProviderImpl
import com.foodsaver.app.domain.repository.DatabaseProvider
import org.koin.dsl.module

val databaseModule = module {

    single<SqlDriverFactory> {
        SqlDriverFactory()
    }
    single<DatabaseProvider> {
        DatabaseProviderImpl(get())
    }
}