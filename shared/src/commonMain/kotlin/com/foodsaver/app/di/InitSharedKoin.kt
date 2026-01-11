package com.foodsaver.app.di

import com.foodsaver.app.featureProductDetail.di.featureProductDetailModule
import org.koin.core.module.Module

fun initSharedKoin(modules: Array<Module> = arrayOf()) = initKoinApp(
    arrayOf(
        *modules,
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