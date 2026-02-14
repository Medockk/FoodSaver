package com.foodsaver.app.coreSettings.data

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore
import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider
import com.foodsaver.app.coreSettings.domain.repository.LocaleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

private val languageKey = stringPreferencesKey(LocaleRepository.DATASTORE_LANGUAGE_KEY)
context(scope: LocaleRepositoryImpl)
internal actual fun ExpectedDataStore.getCurrentLocale(): Flow<String> {
    return dataStore.data.map {
        val locale = it[languageKey] ?: DefaultLocaleProvider.DEFAULT_LOCALE
        println("LOCALE $locale")
        println("java.util.Locale: ${Locale.getDefault()}")
        locale
    }
}

context(scope: LocaleRepositoryImpl)
internal actual suspend fun ExpectedDataStore.setCurrentLocale(
    languageCode: String
) {
    this.dataStore.edit {
        it[languageKey] = languageCode
    }
}