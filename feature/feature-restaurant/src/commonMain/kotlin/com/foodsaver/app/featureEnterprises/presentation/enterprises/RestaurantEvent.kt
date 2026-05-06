package com.foodsaver.app.featureEnterprises.presentation.enterprises

sealed interface RestaurantEvent {

    data class OnSelectedImageIndexChange(val index: Int): RestaurantEvent

}