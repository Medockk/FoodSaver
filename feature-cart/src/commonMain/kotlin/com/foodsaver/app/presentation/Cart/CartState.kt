package com.foodsaver.app.presentation.Cart

import com.foodsaver.app.domain.model.CartItemModel

data class CartState(
    val cartProducts: List<CartItemModel> = emptyList(),

    val isLoading: Boolean = false,
)
