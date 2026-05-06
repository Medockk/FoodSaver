package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
import kotlin.time.Clock
import kotlin.time.Instant

fun ProductModel.toDto() =
    ProductDto(
        productId = productId,
        name = name,
        description = description,
        imageUris = imageUris,
        expiresAt = expiresAt,
        price = price,
        discount = discount,
        count = count,
        unit = unit,
        currency = currency,
        restaurantId = restaurantId,
        categoryIds = categoryIds,
        ingredientIds = ingredientIds,
        isDeleted = isDeleted,
        isAvailable = isAvailable
    )