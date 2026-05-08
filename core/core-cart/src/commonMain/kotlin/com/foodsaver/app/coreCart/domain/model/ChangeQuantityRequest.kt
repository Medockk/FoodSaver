package com.foodsaver.app.coreCart.domain.model

data class ChangeQuantityRequest(
    val cartItemId: String,
    val newQuantity: Long
)
