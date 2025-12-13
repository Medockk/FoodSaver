package com.foodsaver.app.presentation.ProductDetail

sealed interface ProductDetailActions {

    data class OnError(val message: String): ProductDetailActions
}