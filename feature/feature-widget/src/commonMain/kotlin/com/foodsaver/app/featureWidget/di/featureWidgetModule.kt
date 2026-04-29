package com.foodsaver.app.featureWidget.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformFeatureWidgetModule: Module

val featureWidgetModule = module {
    includes(platformFeatureWidgetModule)
}