package com.foodsaver.app.coreSettings.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocaleRepository {

    fun getCurrentLocale(): Flow<String>
    suspend fun setCurrentLocale(languageCode: String)

    companion object {
        const val DATASTORE_LANGUAGE_KEY = "LanguageKey"
    }
}