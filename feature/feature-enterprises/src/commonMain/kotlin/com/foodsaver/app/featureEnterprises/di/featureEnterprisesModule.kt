package com.foodsaver.app.featureEnterprises.di

import com.foodsaver.app.featureEnterprises.data.repository.EnterprisesRepositoryImpl
import com.foodsaver.app.featureEnterprises.domain.repository.EditEnterpriseRepository
import com.foodsaver.app.featureEnterprises.domain.repository.EnterprisesRepository
import com.foodsaver.app.featureEnterprises.domain.usecase.UploadEnterpriseImageUseCase
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureEnterprisesModule = module {

    singleOf(::EnterprisesRepositoryImpl)
    single<EnterprisesRepository> {
        get<EnterprisesRepositoryImpl>()
    }
    single<EditEnterpriseRepository> { get<EnterprisesRepositoryImpl>() }
    factoryOf(::UploadEnterpriseImageUseCase)

    viewModelOf(::EnterprisesViewModel)
}