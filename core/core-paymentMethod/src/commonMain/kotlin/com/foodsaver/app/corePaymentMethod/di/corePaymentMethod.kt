package com.foodsaver.app.corePaymentMethod.di

import com.foodsaver.app.corePaymentMethod.data.PaymentMethodRepositoryImpl
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.usecase.AddPaymentMethodUseCase
import com.foodsaver.app.corePaymentMethod.domain.usecase.RemovePaymentMethodUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val corePaymentMethod = module {
    singleOf(::PaymentMethodRepositoryImpl)

    single<ReadPaymentMethodRepository> {
        get<PaymentMethodRepositoryImpl>()
    }
    single<EditPaymentMethodRepository> {
        get<PaymentMethodRepositoryImpl>()
    }

    factoryOf(::AddPaymentMethodUseCase)
    factoryOf(::RemovePaymentMethodUseCase)
}