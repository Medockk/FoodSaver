package com.foodsaver.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemDto(
    val id: String,
    val product: ProductDto,
    val quantity: Int,
    val tempId: String,
)
