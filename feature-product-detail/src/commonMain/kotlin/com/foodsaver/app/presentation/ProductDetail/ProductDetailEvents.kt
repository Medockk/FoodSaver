package com.foodsaver.app.presentation.ProductDetail

sealed interface ProductDetailEvents {

    data object OnAddProductToCart: ProductDetailEvents
    data object OnRemoveProductFromCart: ProductDetailEvents

    data object OnIncreaseCountClick: ProductDetailEvents
    data object OnDecreaseCountClick: ProductDetailEvents
}