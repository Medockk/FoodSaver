package com.foodsaver.app.featureFoodDetail.di

import com.foodsaver.app.featureFoodDetail.data.repository.IngredientRepositoryImpl
import com.foodsaver.app.featureFoodDetail.domain.repository.IngredientsRepository
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val featureFoodDetailModule = module {

    single<IngredientRepositoryImpl>() bind IngredientsRepository::class

    viewModelOf(::FoodDetailViewModel)
}