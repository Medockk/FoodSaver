package com.foodsaver.app.coreCart.domain.model

data class CartRequestModel(
    val productId: String,
    val quantity: Long? =  null
)
