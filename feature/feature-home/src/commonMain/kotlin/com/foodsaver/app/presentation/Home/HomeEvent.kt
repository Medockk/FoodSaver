package com.foodsaver.app.presentation.Home

import androidx.compose.ui.text.input.TextFieldValue

sealed interface HomeEvent {

    data class OnSearchQueryChange(val value: TextFieldValue): HomeEvent
    data class OnCategoryIndexChange(val value: String): HomeEvent
    data object LoadNextRestaurants: HomeEvent
}