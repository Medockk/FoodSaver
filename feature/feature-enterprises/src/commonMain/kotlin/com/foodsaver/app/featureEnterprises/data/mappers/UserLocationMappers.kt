package com.foodsaver.app.featureEnterprises.data.mappers

import com.foodsaver.app.featureEnterprises.data.dto.UserLocationDto
import com.foodsaver.app.featureEnterprises.domain.model.UserLocationModel

internal fun UserLocationModel.mapToDto() = UserLocationDto(
    latitude = latitude,
    longitude = longitude
)