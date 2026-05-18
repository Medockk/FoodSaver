package com.foodsaver.app.coreAddress.data.mappers

import com.databases.cache.AddressEntity
import com.foodsaver.app.coreAddress.data.dto.AddAddressDto
import com.foodsaver.app.coreAddress.data.dto.AddressDto
import com.foodsaver.app.coreAddress.domain.model.AddAddressRequest
import com.foodsaver.app.coreAddress.domain.model.AddressModel

internal fun AddAddressRequest.mapToDto() =
    AddAddressDto(
        name = name,
        latitude = latitude,
        longitude = longitude,
        city = city,
        street = street,
        house = house,
        apartment = apartment,
        floor = floor,
        entrance = entrance
    )

internal fun AddressDto.mapDtoToEntity(userId: String) = AddressEntity(
    id = id,
    name = name,
    userId = userId,
    latitude = latitude,
    longitude = longitude,
    city = city,
    street = street,
    house = house,
    apartment = apartment,
    floor = floor?.toLong(),
    entrance = entrance,
    fullAddress = fullAddress
)

internal fun AddressEntity.mapEntityToModel() = AddressModel(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    city = city,
    street = street,
    house = house,
    apartment = apartment,
    floor = floor?.toInt(),
    entrance = entrance,
    fullAddress = fullAddress
)