package com.foodsaver.app.coreSettings.data

import com.foodsaver.app.coreSettings.data.factory.DataStoreFactory
import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore
import com.foodsaver.app.coreSettings.domain.repository.LocaleRepository
import kotlinx.coroutines.flow.Flow

internal class LocaleRepositoryImpl(
    private val dataStoreFactory: DataStoreFactory
): LocaleRepository {

    private val currentLocale: ExpectedDataStore = dataStoreFactory.createDataStore()

    override fun getCurrentLocale(): Flow<String> {
        return currentLocale.getCurrentLocale()
    }

    override suspend fun setCurrentLocale(languageCode: String) {
        currentLocale.setCurrentLocale(languageCode)
    }

}


context(scope: LocaleRepositoryImpl)
internal expect fun ExpectedDataStore.getCurrentLocale(): Flow<String>

context(scope: LocaleRepositoryImpl)
internal expect suspend fun ExpectedDataStore.setCurrentLocale(languageCode: String)