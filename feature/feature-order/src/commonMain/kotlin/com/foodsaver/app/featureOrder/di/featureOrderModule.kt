package com.foodsaver.app.featureOrder.di

import com.foodsaver.app.featureOrder.data.repository.OrderRepositoryImpl
import com.foodsaver.app.featureOrder.domain.repository.OrderRepository
import com.foodsaver.app.featureOrder.presentation.OrderViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val featureOrderModule = module {

    single<OrderRepositoryImpl>() bind OrderRepository::class
    viewModelOf(::OrderViewModel)
}