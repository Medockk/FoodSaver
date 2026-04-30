package com.foodsaver.app.coreEnterprises.data.mappers

import com.foodsaver.app.coreEnterprises.data.dto.UserLocationDto
import com.foodsaver.app.coreEnterprises.domain.model.UserLocationModel

internal fun UserLocationModel.mapToDto() = UserLocationDto(
    latitude = latitude,
    longitude = longitude
)