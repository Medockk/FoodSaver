package com.foodsaver.app.presentation.Home

sealed interface HomeEvent {

    data class OnSearchQueryChange(val value: String): HomeEvent
    data class OnCategoryIndexChange(val value: String): HomeEvent
    data class OnAddProductToCart(val productId: String): HomeEvent

    data object LoadNextProducts: HomeEvent
}