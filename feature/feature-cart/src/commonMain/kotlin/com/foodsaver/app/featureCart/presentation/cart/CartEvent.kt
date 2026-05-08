package com.foodsaver.app.featureCart.presentation.cart

sealed interface CartEvent {

    data object OnPlaceOrderClick: CartEvent
    data class IncreaseProductClick(val item: CartState.CartItem): CartEvent
    data class DecreaseProductClick(val item: CartState.CartItem): CartEvent

    data object OnEditItemsClick: CartEvent

    data object OnEditDeliveryAddressClick: CartEvent
    data class OnAddressValueChange(val value: String): CartEvent
}