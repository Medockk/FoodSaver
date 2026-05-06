package com.foodsaver.app.coreEnterprises.data.mappers

import com.foodsaver.app.coreModel.mappers.toModel
import com.foodsaver.app.coreEnterprises.data.dto.RestaurantDto
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel

internal fun RestaurantDto.mapToModel() = RestaurantModel(
    id = id,
    latitude = latitude,
    longitude = longitude,
    addressName = addressName,
    companyId = companyId,
    photoUris = photoUris,

    description = description,
    rating = rating,
    deliveryCost = deliveryCost,
    averageDeliveryTime = averageDeliveryTime,
    name = name
)