package com.foodsaver.app.coreCart.data.mappers

import com.databases.cache.CartEntity
import com.foodsaver.app.coreCart.data.dto.CartResponseDto
import com.foodsaver.app.coreCart.domain.model.CartResponseModel

internal fun CartEntity.mapCartEntityToModel() = CartResponseModel(
    cartId = this.id,
    quantity = this.totalQuantity,
    finalPrice = price,
)

internal fun CartResponseDto.mapDtoToModel() = CartResponseModel(
    cartId = this.id,
    quantity = this.quantity,
    finalPrice = finalPrice
)