package com.foodsaver.app.di

import com.foodsaver.app.data.factory.SqlDriverFactory
import com.foodsaver.app.data.repository.UserDatabaseApiImpl
import com.foodsaver.app.domain.repository.UserDatabaseApi
import com.foodsaver.app.domain.usecase.GetAllUsersUseCase
import com.foodsaver.app.domain.usecase.GetUserByUidUseCase
import com.foodsaver.app.domain.usecase.InsertUserUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val databaseModule = module {

    single<SqlDriverFactory> {
        SqlDriverFactory()
    }
    single<UserDatabaseApi> {
        UserDatabaseApiImpl(
            sqlDriverFactory = get()
        )
    }

    factoryOf(::GetAllUsersUseCase)
    factoryOf(::GetUserByUidUseCase)
    factory { InsertUserUseCase(get()) }
}