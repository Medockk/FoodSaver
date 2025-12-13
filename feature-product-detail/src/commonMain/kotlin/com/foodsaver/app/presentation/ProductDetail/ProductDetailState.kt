package com.foodsaver.app.presentation.ProductDetail

import com.foodsaver.app.domain.model.ProductModel

data class ProductDetailState(
    val product: ProductModel? = null,

    val productCount: Int = 1,
    val totalCost: Float? = null
)
