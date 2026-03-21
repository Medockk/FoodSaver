package com.foodsaver.app.androidApp

import android.app.Application
import android.util.Log
import com.foodsaver.app.di.initSharedKoin
import com.foodsaver.app.di.uiModule
import com.foodsaver.app.presentation.featureEnterprise.MapKit

class FoodSaverApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        com.foodsaver.app.di.applicationContext = this.applicationContext
        initSharedKoin(arrayOf(uiModule))

        try {
            MapKit.setApiKey(this.applicationContext)
        } catch (e: Exception) {
            Log.e("onCreate", "Exception when initializing map kit + set map kit api key", e)
        }
    }
}