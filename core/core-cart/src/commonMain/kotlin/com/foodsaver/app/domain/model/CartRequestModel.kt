package com.foodsaver.app.domain.model

data class CartRequestModel(
    val productId: String,
    val quantity: Long? =  null
)
