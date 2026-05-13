package com.foodsaver.app.featurePaymentMethod.di

import com.foodsaver.app.featurePaymentMethod.presentation.addCard.AddCardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featurePaymentMethodModule = module {

    viewModelOf(::AddCardViewModel)
}