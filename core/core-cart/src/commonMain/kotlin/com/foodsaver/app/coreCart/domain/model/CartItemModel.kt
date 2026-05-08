package com.foodsaver.app.coreCart.domain.model

data class CartItemModel(
    val cartItemId: String,
    val productId: String,
    val quantity: Long
)
