package com.foodsaver.app.presentation.Home

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface HomeAction : AppAction {

    data class OnError(val message: String) : HomeAction
    data class OnProductNavigation(
        val productId: String,
        val isProductInCart: Boolean,
        val cartProductCount: Long?,
    ) : HomeAction
}