package com.foodsaver.app.featureOrder.domain.model

data class OrderItemModel(
    val id: String,
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Long
)
