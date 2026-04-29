@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.coreProfile.data.mappers

import com.databases.cache.UserEntity
import com.foodsaver.app.coreModel.dto.UserDto
import com.foodsaver.app.coreProfile.domain.model.UserModel
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
        bio = bio
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
        bio = bio
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
        bio = bio
    )