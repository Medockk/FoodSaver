package com.foodsaver.app.presentation.Cart

import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.coreProfile.domain.model.UserModel

data class CartState(
    val cartProducts: List<CartItemModel> = emptyList(),
    val profile: UserModel? = null,
    val paymentMethod: PaymentMethodModel? = null,

    val isLoading: Boolean = false,
)
