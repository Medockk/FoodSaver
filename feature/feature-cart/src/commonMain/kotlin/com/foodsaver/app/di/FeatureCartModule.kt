package com.foodsaver.app.di

import com.foodsaver.app.presentation.Cart.CartViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureCartModule = module {

    viewModelOf(::CartViewModel)
}