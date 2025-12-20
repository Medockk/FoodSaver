package com.foodsaver.app.di

fun initSharedKoin() = initKoinApp(
    arrayOf(
        networkModule,
        databaseModule,
        *featureAuthModule,
        featureMainModule,
        coreProductModule,
        featureProductDetailModule,
        coreCartModule,
        featureCartModule,
        coreProfileModule,
        featureProfileModule
    )
)