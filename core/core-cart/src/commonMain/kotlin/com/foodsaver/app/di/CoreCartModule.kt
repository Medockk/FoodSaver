package com.foodsaver.app.di

import com.foodsaver.app.data.repository.CartRepositoryImpl
import com.foodsaver.app.domain.repository.CartRepository
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.DecreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.GetCartUseCase
import com.foodsaver.app.domain.usecase.IncreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.RemoveProductFromCartUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreCartModule = module {

    single<CartRepository> {
        CartRepositoryImpl(
            httpClient = get(),
            databaseProvider = get()
        )
    }

    factoryOf(::GetCartUseCase)
    factoryOf(::AddProductToCartUseCase)
    factoryOf(::IncreaseProductCountUseCase)
    factoryOf(::DecreaseProductCountUseCase)
    factoryOf(::RemoveProductFromCartUseCase)
}