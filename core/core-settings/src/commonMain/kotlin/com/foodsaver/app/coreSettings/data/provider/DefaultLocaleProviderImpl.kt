package com.foodsaver.app.coreSettings.data.provider

import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider

internal expect class DefaultLocaleProviderImpl(): DefaultLocaleProvider {

    override fun getDefaultLocale(): String
}