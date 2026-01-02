package com.foodsaver.app.presentation.ProductDetail

import com.foodsaver.app.model.ProductModel

data class ProductDetailState(
    val product: ProductModel? = null,

    val productCount: Long = 1,
    val totalCost: Float? = null,

    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isProductInCart: Boolean = false,
)
