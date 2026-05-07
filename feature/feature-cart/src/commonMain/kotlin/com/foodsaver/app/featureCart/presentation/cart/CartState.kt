package com.foodsaver.app.featureCart.presentation.cart

import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProfile.domain.model.UserModel
import com.foodsaver.app.domain.model.CartItemModel

data class CartState(

    val isItemsEdit: Boolean = false,
    val isDeliveryAddressEdit: Boolean = false,
    val totalCost: Double = 0.0,
    val deliveryAddress: String = "",
    val products: List<ProductModel> = emptyList(),


    val cartProducts: List<CartItemModel> = emptyList(),
    val profile: UserModel? = null,
    val paymentMethod: PaymentMethodModel? = null,

    val isLoading: Boolean = false,
)
