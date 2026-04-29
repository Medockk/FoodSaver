package com.foodsaver.app.coreAddress.data.mappers

import com.databases.cache.AddressEntity
import com.foodsaver.app.coreAddress.data.dto.AddAddressDto
import com.foodsaver.app.coreAddress.domain.model.AddAddressModel
import com.foodsaver.app.coreModel.dto.AddressDto
import com.foodsaver.app.coreModel.model.AddressModel

internal fun AddressEntity.mapToModel() =
    AddressModel(
        id = globalId,
        name = name ?: address,
        address = address,
        isCurrentAddress = isCurrentAddress
    )

internal fun AddressDto.mapToModel() =
    AddressModel(
        id = id,
        name = name,
        address = address,
        isCurrentAddress = isCurrentAddress
    )

internal fun AddAddressModel.mapToDto() =
    AddAddressDto(
        name = name,
        address = address,
        isCurrentAddress = isCurrentAddress
    )