package com.foodsaver.app.coreSettings.data.factory

import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore

internal interface DataStoreFactory {

    fun createDataStore(): ExpectedDataStore

    companion object {
        const val DEFAULT_DATASTORE_NAME = "FoodSaver_DataStore.preferences_pb"
    }
}