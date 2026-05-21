package com.foodsaver.app.featureCart.presentation.cart

import com.foodsaver.app.coreCart.domain.model.CartItemModel

data class CartState(

    val isItemsEditing: Boolean = false,
    val isDeliveryAddressEditing: Boolean = false,
    val totalCost: Double = 0.0,
    val deliveryAddress: String = "",
    val products: List<CartItemModel> = emptyList(),
    val currency: String = "",

    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
)