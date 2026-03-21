package com.foodsaver.app.presentation.Cart

sealed interface CartEvent {

    data class OnIncreaseProductCount(val productId: String): CartEvent
    data class OnDecreaseProductCount(val productId: String): CartEvent

    data class OnDeleteProduct(val productId: String): CartEvent
    data object OnClearCart: CartEvent
}