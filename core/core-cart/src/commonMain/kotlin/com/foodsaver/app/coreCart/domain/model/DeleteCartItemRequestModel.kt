package com.foodsaver.app.coreCart.domain.model

data class DeleteCartItemRequestModel(
    val localId: String,
    val cartItemId: String?,
)
