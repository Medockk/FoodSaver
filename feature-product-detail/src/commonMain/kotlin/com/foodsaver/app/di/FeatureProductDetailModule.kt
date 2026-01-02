package com.foodsaver.app.di

import com.foodsaver.app.presentation.ProductDetail.ProductDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureProductDetailModule = module {

    viewModel { params ->
        ProductDetailViewModel(
            productId = params.get<String>(),
            isProductInCart = params.get<Boolean>(),
            getCachedProductUseCase = get(),
            addProductToCartUseCase = get(),
            increaseProductCountUseCase = get(),
            decreaseProductCountUseCase = get(),
            removeProductFromCartUseCase = get(),
        )
    }
}