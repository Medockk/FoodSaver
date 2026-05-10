package com.foodsaver.app.coreCart.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ProductInCartResponseDto(
    val productId: String,
    val cartItemId: String
)
