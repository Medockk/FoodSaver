package com.foodsaver.app.coreRestaurant.di

import com.foodsaver.app.coreRestaurant.data.repository.RestaurantRepositoryImpl
import com.foodsaver.app.coreRestaurant.domain.repository.EditRestaurantRepository
import com.foodsaver.app.coreRestaurant.domain.repository.RestaurantRepository
import com.foodsaver.app.coreRestaurant.domain.usecase.UploadEnterpriseImageUseCase
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