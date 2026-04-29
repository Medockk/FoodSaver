package com.foodsaver.app.featureProductDetail.presentation.productDetail

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProductDetailActions: AppAction {

    data class OnError(val message: String): ProductDetailActions
    data object OnAddedToCart: ProductDetailActions
}