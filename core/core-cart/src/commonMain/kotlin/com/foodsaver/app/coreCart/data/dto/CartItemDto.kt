package com.foodsaver.app.coreCart.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CartItemDto(
    @SerialName("id")
    val cartItemId: String,
    val productId: String,
    val quantity: Long
)
