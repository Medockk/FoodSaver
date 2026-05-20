package com.foodsaver.app.coreIngredients.di

import com.foodsaver.app.coreIngredients.data.repository.IngredientRepositoryImpl
import com.foodsaver.app.coreIngredients.domain.repository.IngredientRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val coreIngredientsModule = module {

    single<IngredientRepositoryImpl>() bind IngredientRepository::class
}