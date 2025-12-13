package com.foodsaver.app.di

import com.foodsaver.app.presentation.ProductDetail.ProductDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureProductDetailModule = module {

    viewModel { params ->
        ProductDetailViewModel(
            productId = params.get<String>(),
            getCachedProductUseCase = get()
        )
    }
}