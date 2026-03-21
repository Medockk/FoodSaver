package com.foodsaver.app.coreLocation.data.repository

import com.foodsaver.app.coreLocation.domain.model.LocationModel
import com.foodsaver.app.coreLocation.domain.repository.LocationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

internal actual class LocationServiceImpl :
    LocationService {
    actual override fun getCurrentLocation(): Flow<LocationModel> = channelFlow {

    }
}