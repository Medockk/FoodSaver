package com.foodsaver.app.di

import com.foodsaver.app.client.HttpClientType
import com.foodsaver.app.data.repository.CategoryRepositoryImpl
import com.foodsaver.app.data.repository.ProductRepositoryImpl
import com.foodsaver.app.domain.repository.CategoryRepository
import com.foodsaver.app.domain.repository.ProductRepository
import com.foodsaver.app.domain.usecase.GetAllCategoriesUseCase
import com.foodsaver.app.domain.usecase.GetProductsUseCase
import com.foodsaver.app.presentation.Home.HomeViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val featureMainModule = module {
    single<CategoryRepository> {
        CategoryRepositoryImpl(
            httpClient = get<HttpClient>(qualifier = named( HttpClientType.MAIN_CLIENT))
        )
    }

    single<ProductRepository> {
        ProductRepositoryImpl(
            httpClient = get(named(HttpClientType.MAIN_CLIENT))
        )
    }

    factoryOf(::GetAllCategoriesUseCase)
    factoryOf(::GetProductsUseCase)
    viewModelOf(::HomeViewModel)
}