package com.foodsaver.app.coreProfile.data.mappers

import com.databases.cache.UserEntity
import com.foodsaver.app.coreProfile.data.dto.ProfileDto
import com.foodsaver.app.coreProfile.data.dto.UpdateProfileDto
import com.foodsaver.app.coreProfile.domain.model.ProfileModel
import com.foodsaver.app.coreProfile.domain.model.UpdateProfileRequest

internal fun ProfileDto.mapDtoToEntity() = UserEntity(
    id = id,
    email = email,
    fullName = fullName,
    imageUri = imageUri,
    restaurantId = restaurantId,
    authorities = authorities,
    phone = phone,
    bio = bio,
    currentAddressId = currentAddressId,
    currentPaymentMethodId = currentPaymentMethodId,
    addressIds = addressIds,
)

internal fun ProfileDto.mapDtoToResponse() = ProfileModel(
    id = id,
    email = email,
    fullName = fullName,
    imageUri = imageUri,
    restaurantId = restaurantId,
    authorities = authorities,
    phone = phone,
    bio = bio,
    addressIds = addressIds,
    currentAddressId = currentAddressId,
    currentPaymentMethodId = currentPaymentMethodId,
)

internal fun UserEntity.mapEntityToModel() = ProfileModel(
    id = id,
    email = email,
    fullName = fullName,
    imageUri = imageUri,
    restaurantId = restaurantId,
    authorities = authorities,
    phone = phone,
    bio = bio,
    addressIds = addressIds,
    currentAddressId = currentAddressId,
    currentPaymentMethodId = currentPaymentMethodId,
)

internal fun UpdateProfileRequest.mapRequestToDto() = UpdateProfileDto(
    fullName = fullName,
    phone = phone,
    bio = bio,
    email = email
)