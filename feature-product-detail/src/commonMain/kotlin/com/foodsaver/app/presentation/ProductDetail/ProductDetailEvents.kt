package com.foodsaver.app.presentation.ProductDetail

sealed interface ProductDetailEvents {

    data object AddProductToCart: ProductDetailEvents

    data object OnIncreaseCountClick: ProductDetailEvents
    data object OnDecreaseCountClick: ProductDetailEvents
}