package com.foodsaver.app.di

import com.foodsaver.app.data.repository.CategoryRepositoryImpl
import com.foodsaver.app.data.repository.OfferRepositoryImpl
import com.foodsaver.app.domain.repository.CategoryRepository
import com.foodsaver.app.domain.repository.OfferRepository
import com.foodsaver.app.domain.usecase.GetAllCategoriesUseCase
import com.foodsaver.app.domain.usecase.offer.GetOffersUseCase
import com.foodsaver.app.presentation.Home.HomeViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureMainModule = module {
    single<CategoryRepository> {
        CategoryRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    single<OfferRepository> {
        OfferRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }

    factoryOf(::GetAllCategoriesUseCase)
    factoryOf(::GetOffersUseCase)
    viewModelOf(::HomeViewModel)
}