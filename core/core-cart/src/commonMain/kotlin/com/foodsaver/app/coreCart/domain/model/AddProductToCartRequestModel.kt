package com.foodsaver.app.coreCart.domain.model

data class AddProductToCartRequestModel(
    val productId: String,
    val quantity: Long? =  null,
    val attributes: CartItemAttributes
)
