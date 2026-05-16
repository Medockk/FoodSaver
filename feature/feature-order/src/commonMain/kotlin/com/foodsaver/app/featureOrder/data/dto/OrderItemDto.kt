package com.foodsaver.app.featureOrder.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class OrderItemDto(
    val id: String,
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Long
)
