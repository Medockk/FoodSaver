package com.foodsaver.app.coreAddress.data.mappers

import com.foodsaver.app.coreAddress.data.dto.AddAddressDto
import com.foodsaver.app.coreAddress.data.dto.AddressDto
import com.foodsaver.app.coreAddress.domain.model.AddAddressRequest
import com.foodsaver.app.coreAddress.domain.model.AddressModel

internal fun AddAddressRequest.mapToDto() =
    AddAddressDto(
        latitude = latitude,
        longitude = longitude,
        city = city,
        street = street,
        house = house,
        apartment = apartment,
        floor = floor,
        entrance = entrance
    )

internal fun AddressDto.mapDtoToResponse() = AddressModel(
    id = id,
    latitude = latitude,
    longitude = longitude,
    city = city,
    street = street,
    house = house,
    apartment = apartment,
    floor = floor,
    entrance = entrance,
    fullAddress = fullAddress
)