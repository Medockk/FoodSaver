package com.foodsaver.app.coreSettings.data.factory

import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore

internal expect class DataStoreFactoryImpl: DataStoreFactory {

    override fun createDataStore(): ExpectedDataStore
}