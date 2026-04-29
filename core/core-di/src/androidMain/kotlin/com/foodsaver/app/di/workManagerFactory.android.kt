package com.foodsaver.app.di

import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinApplication

actual fun KoinApplication.workManagerFactory() {
    workManagerFactory()
}