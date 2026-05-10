package com.foodsaver.app.coreCart.data.mappers

import com.foodsaver.app.coreCart.data.dto.ChangeQuantityRequestDto
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest

internal fun ChangeQuantityRequest.mapModelToDto() = ChangeQuantityRequestDto(
    cartItemId = cartItemId ?: throw NullPointerException(),
    newQuantity = newQuantity,
)