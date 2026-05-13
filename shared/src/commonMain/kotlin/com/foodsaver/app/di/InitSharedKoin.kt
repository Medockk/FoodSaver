package com.foodsaver.app.di

//import com.foodsaver.app.featureWidget.di.featureWidgetModule
import com.foodsaver.app.addProductModule.di.featureAddProductModule
import com.foodsaver.app.coreAddress.di.coreAddressModule
import com.foodsaver.app.coreAuth.coreAuthModule
import com.foodsaver.app.coreCart.di.coreCartModule
import com.foodsaver.app.coreCategory.di.coreCategoryModule
import com.foodsaver.app.coreDb.di.databaseModule
import com.foodsaver.app.coreEnterprises.di.coreRestaurantModule
import com.foodsaver.app.coreFcm.di.coreFcmModule
import com.foodsaver.app.coreLocation.di.coreLocationModule
import com.foodsaver.app.corePaymentMethod.di.corePaymentMethod
import com.foodsaver.app.coreProductModule.di.coreProductModule
import com.foodsaver.app.coreProfile.di.coreProfileModule
import com.foodsaver.app.coreSettings.di.coreSettingsModule
import com.foodsaver.app.featureCart.di.featureCartModule
import com.foodsaver.app.featureEnterprises.di.featureRestaurantModule
import com.foodsaver.app.featureFoodDetail.di.featureFoodDetailModule
import com.foodsaver.app.featurePaymentMethod.di.featurePaymentMethodModule
import com.foodsaver.app.featureSearch.di.featureSearchModule
import org.koin.core.module.Module

expect fun test()
fun initSharedKoin(modules: Array<Module> = arrayOf()) = initKoinApp(
    arrayOf(
        *modules,
        networkModule,
        coreAuthModule,
        databaseModule,
        *featureAuthModule,
        featureMainModule,
        coreProductModule,
        featureFoodDetailModule,
        coreCartModule,
        featureCartModule,
        coreProfileModule,
        featureProfileModule,
        corePaymentMethod,
        coreAddressModule,
        coreSettingsModule,
        featureAddProductModule,
        coreCategoryModule,
        featureRestaurantModule,
        coreLocationModule,
        coreFcmModule,
        coreRestaurantModule,
        featureSearchModule,
        featurePaymentMethodModule
//        featureWidgetModule
    )
)

