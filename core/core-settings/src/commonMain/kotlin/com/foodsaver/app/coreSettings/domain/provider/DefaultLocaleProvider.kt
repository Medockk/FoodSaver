package com.foodsaver.app.coreSettings.domain.provider

interface DefaultLocaleProvider {

    fun getDefaultLocale(): String

    companion object {
        const val DEFAULT_LOCALE = "en"
    }
}