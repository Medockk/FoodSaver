package com.foodsaver.app.coreSettings.data.factory

import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore

internal actual class DataStoreFactoryImpl :
    DataStoreFactory {
    actual override fun createDataStore(): ExpectedDataStore {
        return ExpectedDataStore()
    }
}