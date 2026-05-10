package com.foodsaver.app.featureCart.presentation.cart

import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProfile.domain.model.UserModel
import com.foodsaver.app.coreCart.domain.model.CartItemModel

data class CartState(

    val isItemsEditing: Boolean = false,
    val isDeliveryAddressEditing: Boolean = false,
    val totalCost: Double = 0.0,
    val deliveryAddress: String = "",
    val products: List<CartItemModel> = emptyList(),


    val isLoading: Boolean = false,
)