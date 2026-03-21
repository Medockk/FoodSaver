@file:OptIn(ExperimentalForeignApi::class)

package com.foodsaver.app.coreSettings.data.factory

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual class DataStoreFactoryImpl :
    DataStoreFactory {
    actual override fun createDataStore(): ExpectedDataStore {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        val path = requireNotNull(documentDirectory).path + "/${DataStoreFactory.DEFAULT_DATASTORE_NAME}"
        val dataStore = PreferenceDataStoreFactory.createWithPath {
            path.toPath()
        }

        return ExpectedDataStore(dataStore)
    }
}