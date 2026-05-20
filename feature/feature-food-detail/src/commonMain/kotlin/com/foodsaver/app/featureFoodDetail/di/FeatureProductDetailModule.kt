package com.foodsaver.app.featureFoodDetail.di

import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureFoodDetailModule = module {

    viewModelOf(::FoodDetailViewModel)
}