package com.foodsaver.app.coreEnterprises.di

import com.foodsaver.app.coreEnterprises.data.repository.EnterprisesRepositoryImpl
import com.foodsaver.app.coreEnterprises.domain.repository.EditEnterpriseRepository
import com.foodsaver.app.coreEnterprises.domain.repository.EnterprisesRepository
import com.foodsaver.app.coreEnterprises.domain.usecase.UploadEnterpriseImageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreRestaurantModule = module {

    singleOf(::EnterprisesRepositoryImpl)
    single<EnterprisesRepository> {
        get<EnterprisesRepositoryImpl>()
    }
    single<EditEnterpriseRepository> { get<EnterprisesRepositoryImpl>() }
    factoryOf(::UploadEnterpriseImageUseCase)
}