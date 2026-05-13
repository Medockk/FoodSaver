package com.foodsaver.app.coreCart.domain.model

data class CartResponseModel(
    val cartId: String,
    val quantity: Long,
    val finalPrice: Double
)
