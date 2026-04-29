package com.foodsaver.app.coreProductModule.di

import com.foodsaver.app.coreProductModule.data.repository.ProductRepositoryImpl
import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.coreProductModule.domain.usecase.AddProductUseCase
import com.foodsaver.app.coreProductModule.domain.usecase.DeleteProductUseCase
import com.foodsaver.app.coreProductModule.domain.usecase.GetCachedProductUseCase
import com.foodsaver.app.coreProductModule.domain.usecase.GetCachedProductsUseCase
import com.foodsaver.app.coreProductModule.domain.usecase.GetProductsUseCase
import com.foodsaver.app.coreProductModule.domain.usecase.SearchProductUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreProductModule = module {
    single<ProductRepositoryImpl> {
        ProductRepositoryImpl(
            httpClient = get(),
            databaseProvider = get(),
            json = get()
        )
    }

    single<ReadProductRepository> {
        get<ProductRepositoryImpl>()
    }
    single<EditProductRepository> {
        get<ProductRepositoryImpl>()
    }

    factoryOf(::GetCachedProductUseCase)
    factoryOf(::GetCachedProductsUseCase)
    factoryOf(::GetProductsUseCase)
    factoryOf(::SearchProductUseCase)
    factoryOf(::AddProductUseCase)
    factoryOf(::DeleteProductUseCase)
}