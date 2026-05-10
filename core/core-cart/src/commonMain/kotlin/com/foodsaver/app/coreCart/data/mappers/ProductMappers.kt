package com.foodsaver.app.coreCart.data.mappers

import com.databases.cache.CartItemEntity
import com.databases.cache.ProductCacheEntity
import com.foodsaver.app.coreModel.dto.ProductDto

internal fun ProductDto.mapDtoToEntity() = ProductCacheEntity(
    productId = productId,
    name = name,
    description = description,
    imageUri = imageUris.firstOrNull(),
    price = price,
    discount = discount,
    currency = currency,
    unit = unit,
    restaurantId = restaurantId,
    expiresAt = expiresAt
)