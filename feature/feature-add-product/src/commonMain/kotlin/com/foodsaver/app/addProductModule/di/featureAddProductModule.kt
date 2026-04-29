package com.foodsaver.app.addProductModule.di

import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureAddProductModule = module {

    viewModelOf(::AddProductViewModel)
}