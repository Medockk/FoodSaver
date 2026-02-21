package com.foodsaver.app.di

import com.foodsaver.app.addProductModule.di.featureAddProductModule
import com.foodsaver.app.coreAddress.di.coreAddressModule
import com.foodsaver.app.coreAuth.coreAuthModule
import com.foodsaver.app.coreDb.di.databaseModule
import com.foodsaver.app.corePaymentMethod.di.corePaymentMethod
import com.foodsaver.app.coreProductModule.di.coreProductModule
import com.foodsaver.app.coreProfile.di.coreProfileModule
import com.foodsaver.app.coreSettings.di.coreSettingsModule
import com.foodsaver.app.featureProductDetail.di.featureProductDetailModule
import org.koin.core.module.Module

fun initSharedKoin(modules: Array<Module> = arrayOf()) = initKoinApp(
    arrayOf(
        *modules,
        networkModule,
        coreAuthModule,
        databaseModule,
        *featureAuthModule,
        featureMainModule,
        coreProductModule,
        featureProductDetailModule,
        coreCartModule,
        featureCartModule,
        coreProfileModule,
        featureProfileModule,
        corePaymentMethod,
        coreAddressModule,
        coreSettingsModule,
        featureAddProductModule
    )
)