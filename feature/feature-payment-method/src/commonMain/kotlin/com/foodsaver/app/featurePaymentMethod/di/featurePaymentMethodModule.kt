package com.foodsaver.app.featurePaymentMethod.di

import com.foodsaver.app.featurePaymentMethod.data.OrderRepositoryImpl
import com.foodsaver.app.featurePaymentMethod.domain.OrderRepository
import com.foodsaver.app.featurePaymentMethod.presentation.addCard.AddCardViewModel
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featurePaymentMethodModule = module {

    singleOf(::OrderRepositoryImpl)
    single<OrderRepository> {
        get<OrderRepositoryImpl>()
    }
    viewModelOf(::PaymentMethodViewModel)
    viewModelOf(::AddCardViewModel)
}