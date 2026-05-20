package com.foodsaver.app.addProductModule.di

import com.foodsaver.app.addProductModule.data.repository.AddProductRepositoryImpl
import com.foodsaver.app.addProductModule.domain.repository.AddProductRepository
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val featureAddProductModule = module {

    single<AddProductRepositoryImpl>() bind AddProductRepository::class
    viewModelOf(::AddProductViewModel)
}