package com.foodsaver.app.coreEnterprises.data.mappers

import com.foodsaver.app.coreModel.mappers.toModel
import com.foodsaver.app.coreEnterprises.data.dto.EnterprisesDto
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel

internal fun EnterprisesDto.mapToModel() = RestaurantModel(
    id = id,
    latitude = latitude,
    longitude = longitude,
    addressName = addressName,
    organization = organization.toModel(),
    photoUris = photoUris,

    description = description,
    rating = rating,
    deliveryCost = deliveryCost,
    averageDeliveryTime = averageDeliveryTime,
    name = name
)