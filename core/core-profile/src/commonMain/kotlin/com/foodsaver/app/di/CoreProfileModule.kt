package com.foodsaver.app.di

import com.foodsaver.app.data.repository.ProfileRepositoryImpl
import com.foodsaver.app.domain.repository.ProfileRepository
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreProfileModule = module {

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            httpClient = get(),
            databaseProvider = get(),
            authUserManager = get()
        )
    }

    factoryOf(::GetProfileUseCase)
}