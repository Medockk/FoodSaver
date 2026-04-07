package com.foodsaver.app.featureProductDetail.di

import com.foodsaver.app.featureProductDetail.data.repository.IngredientRepositoryImpl
import com.foodsaver.app.featureProductDetail.domain.repository.IngredientsRepository
import com.foodsaver.app.featureProductDetail.presentation.productDetail.ProductDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val featureProductDetailModule = module {

    single<IngredientRepositoryImpl>() bind IngredientsRepository::class

    viewModelOf(::ProductDetailViewModel)
}