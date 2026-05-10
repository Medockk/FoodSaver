package com.foodsaver.app.coreCart.data.mappers

import com.databases.cache.CartItemEntity
import com.foodsaver.app.coreCart.domain.model.CartItemAttributes
import com.foodsaver.app.coreCart.domain.model.CartItemModel

internal fun CartItemEntity.mapEntityToModel(
    name: String,
    price: Double,
    currency: String,
    imageUri: String,
) = CartItemModel(
    localId = localId,
    serverId = serverId,
    productId = productId,
    name = name,
    price = price,
    currency = currency,
    imageUri = imageUri,
    quantity = quantity,
    attributes = CartItemAttributes(
        size = this.attributes?.size,
        additions = this.attributes?.additions ?: emptyList()
    )
)