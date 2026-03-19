package com.foodsaver.app.coreLocation.di

import android.content.Context
import com.foodsaver.app.coreLocation.data.repository.LocationServiceImpl
import com.foodsaver.app.coreLocation.domain.repository.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformCoreLocationModule: Module = module {
    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(get<Context>())
    }

    single<LocationService> {
        LocationServiceImpl(
            fusedLocationClient = get()
        )
    }
}