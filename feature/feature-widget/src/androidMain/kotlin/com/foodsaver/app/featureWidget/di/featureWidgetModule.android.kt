package com.foodsaver.app.featureWidget.di

import com.foodsaver.app.featureWidget.data.AndroidWidgetSyncWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformFeatureWidgetModule: Module = module {
    workerOf(::AndroidWidgetSyncWorker)
}