package com.foodsaver.app.coreSettings.data.provider

import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider
import java.util.Locale

internal actual class DefaultLocaleProviderImpl actual constructor() :
    DefaultLocaleProvider {
    actual override fun getDefaultLocale(): String {
        return Locale.getDefault().toString()
    }
}