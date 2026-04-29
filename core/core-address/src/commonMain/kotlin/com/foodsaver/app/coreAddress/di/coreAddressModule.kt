package com.foodsaver.app.coreAddress.di

import com.foodsaver.app.coreAddress.data.repository.AddressRepositoryImpl
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository
import com.foodsaver.app.coreAddress.domain.repository.ReadAddressRepository
import com.foodsaver.app.coreAddress.domain.usecase.AddAddressUseCase
import com.foodsaver.app.coreAddress.domain.usecase.RemoveAddressUseCase
import com.foodsaver.app.coreAddress.domain.usecase.SetCurrentAddressUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreAddressModule = module {
    singleOf(::AddressRepositoryImpl)
    single<ReadAddressRepository> { get<AddressRepositoryImpl>() }
    single<EditAddressRepository> { get<AddressRepositoryImpl>() }

    factoryOf(::AddAddressUseCase)
    factoryOf(::RemoveAddressUseCase)
    factoryOf(::SetCurrentAddressUseCase)
}