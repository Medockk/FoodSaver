package com.foodsaver.app.featureOrder.data.dto

import com.foodsaver.app.coreModel.model.order.OrderStatus
import com.foodsaver.app.coreModel.model.order.OrderType
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
internal data class OrderDto(
    val id: String,
    val type: OrderType,
    val status: OrderStatus,
    val restaurantImageUri: String?,
    val restaurantName: String,
    val orderPrice: Double,
    val orderSize: Int,
    val trackNumber: String,
    val createdAt: Instant,
    
    val items: List<OrderItemDto>
)