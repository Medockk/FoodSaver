package com.foodsaver.app.featureEnterprises.di

import com.foodsaver.app.featureEnterprises.presentation.enterprises.RestaurantViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureRestaurantModule = module {

    viewModelOf(::RestaurantViewModel)
}