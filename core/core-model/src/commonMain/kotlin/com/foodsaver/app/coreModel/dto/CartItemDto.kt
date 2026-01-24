package com.foodsaver.app.coreModel.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemDto(
    val id: String,
    val product: ProductDto,
    val quantity: Long,
    val tempId: String,
)
