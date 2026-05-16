package com.foodsaver.app.featureOrder.presentation

sealed interface OrderEvent {

    data class OnTabIndexChange(val index: Int): OrderEvent
}