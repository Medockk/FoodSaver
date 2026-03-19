package com.foodsaver.app.androidApp

import android.app.Application
import com.foodsaver.app.di.initSharedKoin
import com.foodsaver.app.di.uiModule
import com.foodsaver.app.presentation.featureEnterprise.MapKit

class FoodSaverApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        com.foodsaver.app.di.applicationContext = this.applicationContext
        initSharedKoin(arrayOf(uiModule))

        MapKit.setApiKey(this.applicationContext)
    }
}