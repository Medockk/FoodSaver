package com.foodsaver.app.featureAdmin.di

import com.foodsaver.app.featureAdmin.presentation.viewCategory.ViewCategoryViewModel
import com.foodsaver.app.featureAdmin.presentation.viewRrestaurant.ViewRestaurantViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureAdminModule = module {
    viewModelOf(::ViewRestaurantViewModel)
    viewModelOf(::ViewCategoryViewModel)
}