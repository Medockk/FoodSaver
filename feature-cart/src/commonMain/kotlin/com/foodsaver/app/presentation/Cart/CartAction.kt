package com.foodsaver.app.presentation.Cart

sealed interface CartAction {

    data class OnError(val message: String): CartAction
    data class OnProductClick(val productId: String): CartAction
}