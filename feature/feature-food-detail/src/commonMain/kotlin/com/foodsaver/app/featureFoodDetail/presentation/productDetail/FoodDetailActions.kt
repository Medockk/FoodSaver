package com.foodsaver.app.featureFoodDetail.presentation.productDetail

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface FoodDetailActions: AppAction {

    data class OnError(val message: String): FoodDetailActions
    data object OnAddedToCart: FoodDetailActions
}