package com.foodsaver.app.coreCart.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class CartResponseDto(
    val id: String,
    val quantity: Long,
    val itemsPrice: Double,
    val discountPrice: Double,
    val deliveryPrice: Double,
    val finalPrice: Double
)
