package com.foodsaver.app.presentation.Home

import androidx.compose.ui.text.input.TextFieldValue

sealed interface HomeEvent {

    data class OnSearchQueryChange(val value: TextFieldValue): HomeEvent
    data class OnCategoryIndexChange(val value: String): HomeEvent
    data class OnAddProductToCart(val productId: String): HomeEvent
    data class OnOfferClick(val productId: String): HomeEvent
    data class OnProductClick(val productId: String): HomeEvent
    data object LoadNextProducts: HomeEvent
}