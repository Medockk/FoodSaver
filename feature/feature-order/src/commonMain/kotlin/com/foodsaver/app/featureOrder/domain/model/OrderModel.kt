package com.foodsaver.app.featureOrder.domain.model

import com.foodsaver.app.coreModel.model.order.OrderStatus
import com.foodsaver.app.coreModel.model.order.OrderType
import kotlin.time.Instant

data class OrderModel(
    val id: String,
    val type: OrderType,
    val status: OrderStatus,
    val restaurantImageUri: String?,
    val restaurantName: String,
    val orderPrice: Double,
    val orderSize: Long,
    val trackNumber: String,
    val createdAt: Instant,

    val items: List<OrderItemModel>
)
