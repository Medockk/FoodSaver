package com.foodsaver.app.coreCart.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ChangeQuantityRequestDto(
    val cartItemId: String,
    val newQuantity: Long
)
