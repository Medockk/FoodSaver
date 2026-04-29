package com.foodsaver.app.coreLocation.domain.repository

import com.foodsaver.app.coreLocation.domain.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationService {

    fun getCurrentLocation(): Flow<LocationModel>
}