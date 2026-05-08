package com.foodsaver.app.coreCart.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class CartRequestDto(
    val productId: String,
    val quantity: Long = 1
)
