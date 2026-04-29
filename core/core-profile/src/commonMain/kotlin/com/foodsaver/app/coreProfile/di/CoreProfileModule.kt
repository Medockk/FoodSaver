package com.foodsaver.app.coreProfile.di

import com.foodsaver.app.coreProfile.data.repository.ProfileRepositoryImpl
import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
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