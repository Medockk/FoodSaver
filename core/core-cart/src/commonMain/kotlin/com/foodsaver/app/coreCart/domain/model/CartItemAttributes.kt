package com.foodsaver.app.coreCart.domain.model

data class CartItemAttributes(
    val size: String? = null,
    val additions: List<String>? = emptyList()
)
