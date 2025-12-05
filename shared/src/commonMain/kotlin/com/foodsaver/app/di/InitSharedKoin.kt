package com.foodsaver.app.di

import com.foodsaver.app.feature.auth.di.featureAuthModule

fun initSharedKoin() = initKoinApp(
    arrayOf(networkModule, databaseModule, *featureAuthModule,)
)