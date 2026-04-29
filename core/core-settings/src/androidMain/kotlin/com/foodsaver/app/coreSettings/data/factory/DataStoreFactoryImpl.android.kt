package com.foodsaver.app.coreSettings.data.factory

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore
import okio.Path.Companion.toPath

internal actual class DataStoreFactoryImpl(private val context: Context) :
    DataStoreFactory {
    actual override fun createDataStore(): ExpectedDataStore {
        val dataStore = PreferenceDataStoreFactory.createWithPath {
            context.filesDir.resolve(DataStoreFactory.DEFAULT_DATASTORE_NAME)
                .absolutePath.toPath()
        }

        return ExpectedDataStore(dataStore)
    }
}