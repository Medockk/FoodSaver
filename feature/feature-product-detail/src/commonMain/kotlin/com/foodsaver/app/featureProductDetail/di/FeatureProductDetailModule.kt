package com.foodsaver.app.featureProductDetail.di

import com.foodsaver.app.featureProductDetail.presentation.productDetail.ProductDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureProductDetailModule = module {

    viewModelOf(::ProductDetailViewModel)
}