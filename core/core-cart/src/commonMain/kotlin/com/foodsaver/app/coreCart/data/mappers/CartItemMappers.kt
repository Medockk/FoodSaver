package com.foodsaver.app.coreCart.data.mappers

import com.foodsaver.app.coreCart.data.dto.CartItemDto
import com.foodsaver.app.coreCart.data.dto.CartRequestDto
import com.foodsaver.app.coreCart.domain.model.CartItemAttributes
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.AddProductToCartRequestModel

internal fun AddProductToCartRequestModel.mapModelToDto() = CartRequestDto(
    productId = productId,
    quantity = quantity ?: 1L
)

internal fun CartItemDto.mapDtoToModel(
    localId: String,
    name: String,
    price: Double,
    currency: String,
    imageUri: String,
    quantity: Long
) = CartItemModel(
    localId = localId,
    serverId = this.cartItemId,
    productId = this.productId,
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