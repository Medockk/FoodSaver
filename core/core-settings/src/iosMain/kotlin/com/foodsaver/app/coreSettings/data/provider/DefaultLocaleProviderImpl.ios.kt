package com.foodsaver.app.coreSettings.data.provider

import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider
import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

internal actual class DefaultLocaleProviderImpl actual constructor() :
    DefaultLocaleProvider {
    actual override fun getDefaultLocale(): String {
        return (NSLocale.preferredLanguages.firstOrNull() as? String)
            ?: DefaultLocaleProvider.DEFAULT_LOCALE
    }
}