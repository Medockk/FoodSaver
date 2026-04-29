package com.foodsaver.app.coreSettings.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal actual class ExpectedDataStore(
    val dataStore: DataStore<Preferences>
)