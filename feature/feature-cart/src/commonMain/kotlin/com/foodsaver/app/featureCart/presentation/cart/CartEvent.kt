package com.foodsaver.app.featureCart.presentation.cart

import com.foodsaver.app.coreCart.domain.model.CartItemModel

sealed interface CartEvent {

    data object OnPlaceOrderClick: CartEvent
    data class IncreaseProductClick(val item: CartItemModel): CartEvent
    data class DecreaseProductClick(val item: CartItemModel): CartEvent

    data object OnEditItemsClick: CartEvent
    data class OnDeleteItem(val item: CartItemModel): CartEvent

    data object OnEditDeliveryAddressClick: CartEvent
    data class OnAddressValueChange(val value: String): CartEvent
}