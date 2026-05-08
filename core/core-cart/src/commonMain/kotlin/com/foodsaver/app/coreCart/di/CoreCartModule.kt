package com.foodsaver.app.coreCart.di

import com.foodsaver.app.coreCart.data.repository.CartRepositoryImpl
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreCart.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.coreCart.domain.usecase.RemoveProductFromCartUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreCartModule = module {

    single<CartRepository> {
        CartRepositoryImpl(
            httpClient = get(),
            databaseProvider = get()
        )
    }

    factoryOf(::AddProductToCartUseCase)
    factoryOf(::RemoveProductFromCartUseCase)
}