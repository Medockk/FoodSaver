package com.foodsaver.app.featureProductDetail.presentation.productDetail

sealed interface ProductDetailActions {

    data class OnError(val message: String): ProductDetailActions
    data object OnAddedToCart: ProductDetailActions
}