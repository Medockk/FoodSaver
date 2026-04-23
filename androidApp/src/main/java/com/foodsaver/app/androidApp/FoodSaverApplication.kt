package com.foodsaver.app.androidApp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.foodsaver.app.di.initSharedKoin
import com.foodsaver.app.di.uiModule
import com.foodsaver.app.presentation.featureEnterprise.MapKit
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class FoodSaverApplication : Application(), Configuration.Provider, KoinComponent {

    override fun onCreate() {
        super.onCreate()

        com.foodsaver.app.di.applicationContext = this.applicationContext
        initSharedKoin(arrayOf(uiModule))

        try {
            MapKit.setApiKey(this.applicationContext)
        } catch (e: Exception) {
            Log.e("onCreate", "Exception when initializing map kit + set map kit api key", e)
        }

        try {
//            CoroutineScope(Dispatchers.IO).launch {
//                AndroidWidgetProviderImpl(this@FoodSaverApplication.applicationContext).requestPinWidget()
//            }

//            AndroidWidgetSyncWorker.doPeriodicRequest(this.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(get<WorkerFactory>())
            .build()
}