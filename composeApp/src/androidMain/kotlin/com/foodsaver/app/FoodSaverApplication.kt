package com.foodsaver.app

import android.app.Application
import com.foodsaver.app.di.initSharedKoin

class FoodSaverApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        com.foodsaver.app.di.applicationContext = this
        initSharedKoin()
    }
}