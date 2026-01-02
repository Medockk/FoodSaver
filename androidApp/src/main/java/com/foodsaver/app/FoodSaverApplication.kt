package com.foodsaver.app

import android.app.Application
import com.foodsaver.app.di.initSharedKoin
import com.foodsaver.app.di.uiModule

class FoodSaverApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        com.foodsaver.app.di.applicationContext = this
        initSharedKoin(arrayOf(uiModule))
    }
}