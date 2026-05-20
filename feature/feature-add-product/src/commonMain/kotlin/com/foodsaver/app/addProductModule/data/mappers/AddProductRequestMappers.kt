package com.foodsaver.app.addProductModule.data.mappers

import com.foodsaver.app.addProductModule.data.dto.AddProductDto
import com.foodsaver.app.addProductModule.domain.model.AddProductRequest

internal fun AddProductRequest.mapRequestToDto(restaurantId: String) = AddProductDto(
    name = name,
    description = description,
    imageUris = imageUris,
    expiresAt = expiresAt,
    price = price,
    count = count,
    unit = unit,
    discount = discount,
    currency = currency,
    isAvailable = isAvailable,
    restaurantId = restaurantId,
    ingredientIds = ingredientIds,
    categoryIds = categoryIds
)