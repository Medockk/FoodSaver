@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.databases.cache.UserEntity
import com.foodsaver.app.domain.model.AddressModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.dto.AddressDto
import com.foodsaver.app.dto.UserDto
import com.foodsaver.app.mappers.toModel
import kotlin.time.ExperimentalTime

internal fun UserDto.tpModel() =
    UserModel(
        uid = uid,
        username = username,
        email = email,
        name = name,
        photoUrl = photoUrl,
        createdAt = createdAt,
        roles = roles,
        phone = phone,
        bio = bio,
        addresses = addresses.map { it.toModel() },
        paymentCartNumbers = paymentCartNumbers.map { it.toModel() },
        currentCity = currentCity
    )

internal fun AddressDto.toModel() = AddressModel(
    id = id,
    name = name,
    address = address
)

internal fun UserEntity.toModel() =
    UserModel(
        uid = uid,
        username = username,
        email = email,
        name = name,
        photoUrl = photoUrl,
        createdAt = createdAt,
        roles = roles,
        phone = phone,
        bio = bio,
        addresses = addresses.map { it.toModel() },
        paymentCartNumbers = paymentCartNumbers.map { it.toModel() },
        currentCity = currentCity
    )

internal fun UserDto.toEntity() =
    UserEntity(
        uid = uid,
        username = username,
        email = email,
        name = name,
        photoUrl = photoUrl,
        createdAt = createdAt,
        roles = roles,
        phone = phone,
        bio = bio,
        addresses = addresses,
        paymentCartNumbers = paymentCartNumbers,
        currentCity = currentCity
    )