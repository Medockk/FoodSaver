package com.foodsaver.app.featureCart.di

import com.foodsaver.app.featureCart.presentation.cart.CartViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureCartModule = module {

    viewModelOf(::CartViewModel)
}