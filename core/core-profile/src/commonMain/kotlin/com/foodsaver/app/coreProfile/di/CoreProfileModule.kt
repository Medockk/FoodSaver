package com.foodsaver.app.coreProfile.di

import com.foodsaver.app.coreProfile.data.repository.ProfileRepositoryImpl
import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val coreProfileModule = module {

    single<ProfileRepositoryImpl>() bind ProfileRepository::class

    factoryOf(::GetProfileUseCase)
}