@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.coreCart.data.mappers

import com.foodsaver.app.coreCart.data.dto.CartItemDto
import com.foodsaver.app.coreCart.data.dto.CartRequestDto
import com.foodsaver.app.coreCart.data.dto.CartResponseDto
import com.foodsaver.app.coreCart.data.dto.ChangeQuantityRequestDto
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.CartRequestModel
import com.foodsaver.app.coreCart.domain.model.CartResponseModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import kotlin.time.ExperimentalTime

internal fun CartRequestModel.toDto(quantity: Long = 1) =
    CartRequestDto(
        productId = productId,
        quantity = this.quantity ?: quantity
    )

internal fun CartItemDto.toModel() = CartItemModel(
    cartItemId = cartItemId,
    productId = productId,
    quantity = quantity
)

internal fun CartResponseDto.toModel() = CartResponseModel(
    cartId = id,
    quantity = quantity
)

internal fun ChangeQuantityRequest.toDto() = ChangeQuantityRequestDto(
    cartItemId = cartItemId,
    newQuantity = newQuantity
)