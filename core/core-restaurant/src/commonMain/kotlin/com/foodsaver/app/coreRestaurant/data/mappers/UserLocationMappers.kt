package com.foodsaver.app.coreRestaurant.data.mappers

import com.foodsaver.app.coreRestaurant.data.dto.UserLocationDto
import com.foodsaver.app.coreRestaurant.domain.model.UserLocationModel

internal fun UserLocationModel.mapToDto() = UserLocationDto(
    latitude = latitude,
    longitude = longitude
)