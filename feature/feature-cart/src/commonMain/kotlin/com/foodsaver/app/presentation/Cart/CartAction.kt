package com.foodsaver.app.presentation.Cart

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface CartAction: AppAction {

    data class OnError(val message: String): CartAction
    data class OnProductClick(val productId: String): CartAction
}