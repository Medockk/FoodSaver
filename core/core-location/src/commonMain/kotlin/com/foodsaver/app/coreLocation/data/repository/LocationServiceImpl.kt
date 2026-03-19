package com.foodsaver.app.coreLocation.data.repository

import com.foodsaver.app.coreLocation.domain.model.LocationModel
import com.foodsaver.app.coreLocation.domain.repository.LocationService
import kotlinx.coroutines.flow.Flow

internal expect class LocationServiceImpl: LocationService {

    override fun getCurrentLocation(): Flow<LocationModel>
}