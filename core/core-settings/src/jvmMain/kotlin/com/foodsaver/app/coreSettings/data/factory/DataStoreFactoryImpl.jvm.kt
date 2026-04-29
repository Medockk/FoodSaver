package com.foodsaver.app.coreSettings.data.factory

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore
import okio.Path.Companion.toPath
import java.io.File

internal actual class DataStoreFactoryImpl :
    DataStoreFactory {
    actual override fun createDataStore(): ExpectedDataStore {
        val file = File(System.getProperty("java.io.tmpdir"), DataStoreFactory.DEFAULT_DATASTORE_NAME)
        val dataStore = PreferenceDataStoreFactory.createWithPath {
            file.absolutePath.toPath()
        }

        return ExpectedDataStore(dataStore)
    }
}