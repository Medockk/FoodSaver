package com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.restaurant

sealed interface RestaurantEvent {

    data class OnSelectedImageIndexChange(val index: Int): RestaurantEvent

    data class OnAddProductToCart(val productId: String): RestaurantEvent

    data class OnMapViewVisibleChange(val isVisible: Boolean): RestaurantEvent
    data object OnMapKitControllerReady: RestaurantEvent
}