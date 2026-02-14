package com.foodsaver.app.coreSettings.data

import com.foodsaver.app.coreSettings.data.model.ExpectedDataStore
import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

context(scope: LocaleRepositoryImpl)
internal actual fun ExpectedDataStore.getCurrentLocale(): Flow<String> {
    return flow { DefaultLocaleProvider.DEFAULT_LOCALE }
}

context(scope: LocaleRepositoryImpl)
internal actual suspend fun ExpectedDataStore.setCurrentLocale(
    languageCode: String,
) {
}