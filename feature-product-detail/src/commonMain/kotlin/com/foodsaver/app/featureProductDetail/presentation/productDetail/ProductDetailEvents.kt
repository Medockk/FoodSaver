package com.foodsaver.app.featureProductDetail.presentation.productDetail

sealed interface ProductDetailEvents {

    data object OnAddProductToCart: ProductDetailEvents
    data object OnRemoveProductFromCart: ProductDetailEvents

    data object OnIncreaseCountClick: ProductDetailEvents
    data object OnDecreaseCountClick: ProductDetailEvents
}