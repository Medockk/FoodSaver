package com.foodsaver.app.coreRestaurant.data.mappers

import com.databases.cache.RestaurantEntity
import com.foodsaver.app.coreRestaurant.data.dto.RestaurantDto
import com.foodsaver.app.coreRestaurant.data.dto.AddRestaurantDto
import com.foodsaver.app.coreRestaurant.data.dto.UpdateRestaurantDto
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreRestaurant.domain.model.AddRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.model.UpdateRestaurantRequest

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

internal fun RestaurantEntity.mapEntityToDto() = RestaurantModel(
    id = this.serverId,
    latitude = latitude,
    longitude = longitude,
    addressName = addressName,
    companyId = companyId,
    photoUris = photoUris ?: emptyList(),

    description = description,
    rating = rating,
    deliveryCost = deliveryCost,
    averageDeliveryTime = averageDeliveryTime,
    name = name
)

internal fun AddRestaurantRequest.mapRequestToDto() = AddRestaurantDto(
    companyId = companyId,
    name = name,
    description = description,
    photoUris = photoUris,
    rating = rating,
    averageDeliveryTime = averageDeliveryTime,
    deliveryCost = deliveryCost,
    address = AddRestaurantDto.AddressDto(
        addressName = addressName,
        latitude = latitude,
        longitude = longitude
    )
)

internal fun UpdateRestaurantRequest.mapRequestToDto() = UpdateRestaurantDto(
    restaurantId = restaurantId,
    name = name,
    description = description,
    photoUris = photoUris,
    rating = rating,
    averageDeliveryTime = averageDeliveryTime,
    deliveryCost = deliveryCost,
    address = UpdateRestaurantDto.Address(
        addressName = addressName,
        latitude = latitude,
        longitude = longitude
    )
)