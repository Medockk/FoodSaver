package com.foodsaver.app.coreEnterprises.di

import com.foodsaver.app.coreEnterprises.data.repository.RestaurantRepositoryImpl
import com.foodsaver.app.coreEnterprises.domain.repository.EditRestaurantRepository
import com.foodsaver.app.coreEnterprises.domain.repository.RestaurantRepository
import com.foodsaver.app.coreEnterprises.domain.usecase.UploadEnterpriseImageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreRestaurantModule = module {

    singleOf(::RestaurantRepositoryImpl)
    single<RestaurantRepository> {
        get<RestaurantRepositoryImpl>()
    }
    single<EditRestaurantRepository> { get<RestaurantRepositoryImpl>() }
    factoryOf(::UploadEnterpriseImageUseCase)
}