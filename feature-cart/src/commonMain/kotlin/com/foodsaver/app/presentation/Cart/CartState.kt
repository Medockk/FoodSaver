package com.foodsaver.app.presentation.Cart

import com.foodsaver.app.domain.model.ProductModel

data class CartState(
    val cartProducts: List<ProductModel> = emptyList(),

    val isLoading: Boolean = false,
)
