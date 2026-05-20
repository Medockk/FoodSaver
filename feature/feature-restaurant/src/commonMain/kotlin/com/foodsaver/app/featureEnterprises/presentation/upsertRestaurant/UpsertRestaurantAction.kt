package com.foodsaver.app.featureEnterprises.presentation.upsertRestaurant

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface UpsertRestaurantAction: AppAction {

    data class OnError(val message: String): UpsertRestaurantAction
    data object OnRestaurantAdded: UpsertRestaurantAction
}