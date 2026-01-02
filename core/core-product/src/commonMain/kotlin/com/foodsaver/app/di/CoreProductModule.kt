package com.foodsaver.app.di

import com.foodsaver.app.data.repository.ProductRepositoryImpl
import com.foodsaver.app.domain.repository.ProductRepository
import com.foodsaver.app.domain.usecase.GetCachedProductUseCase
import com.foodsaver.app.domain.usecase.GetProductsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreProductModule = module {
    single<ProductRepository> {
        ProductRepositoryImpl(
            httpClient = get(),
            databaseProvider = get()
        )
    }

    factoryOf(::GetCachedProductUseCase)
    factoryOf(::GetProductsUseCase)
}