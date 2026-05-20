package com.foodsaver.app.featureAdmin.presentation.viewRrestaurant

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ViewRestaurantAction: AppAction {

    data class OnError(val message: String): ViewRestaurantAction
}