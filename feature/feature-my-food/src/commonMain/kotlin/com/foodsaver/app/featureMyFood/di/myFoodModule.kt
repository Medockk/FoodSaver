package com.foodsaver.app.featureMyFood.di

import com.foodsaver.app.featureMyFood.presentation.MyFoodViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myFoodModule = module {
    viewModelOf(::MyFoodViewModel)
}