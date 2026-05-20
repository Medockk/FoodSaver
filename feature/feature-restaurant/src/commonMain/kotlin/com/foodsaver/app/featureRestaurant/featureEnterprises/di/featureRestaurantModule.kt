package com.foodsaver.app.featureRestaurant.featureEnterprises.di

import com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.restaurant.RestaurantViewModel
import com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.upsertRestaurant.UpsertRestaurantViewModel
import com.foodsaver.app.featureRestaurant.viewRestaurant.ViewRestaurantViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureRestaurantModule = module {

    viewModelOf(::RestaurantViewModel)
    viewModelOf(::UpsertRestaurantViewModel)
    viewModelOf(::ViewRestaurantViewModel)
}