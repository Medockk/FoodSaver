@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.databases.cache.UserEntity
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.dto.UserDto
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
        addresses = addresses,
        paymentCartNumbers = paymentCartNumbers,
        currentCity = currentCity
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
        addresses = addresses,
        paymentCartNumbers = paymentCartNumbers,
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