package com.foodsaver.app.coreLocation.di

import com.foodsaver.app.coreLocation.data.repository.LocationServiceImpl
import com.foodsaver.app.coreLocation.domain.repository.LocationService
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformCoreLocationModule: Module = module {
    single<LocationService> { LocationServiceImpl() }
}