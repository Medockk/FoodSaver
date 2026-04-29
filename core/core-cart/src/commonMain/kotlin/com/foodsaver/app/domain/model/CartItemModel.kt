package com.foodsaver.app.domain.model

import com.foodsaver.app.coreModel.model.ProductModel

data class CartItemModel(
    val localId: Long,
    val globalId: String?,
    val product: ProductModel,
    val quantity: Long
)
