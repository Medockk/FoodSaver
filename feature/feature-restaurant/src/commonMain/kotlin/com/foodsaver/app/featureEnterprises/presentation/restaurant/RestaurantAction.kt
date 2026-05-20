package com.foodsaver.app.featureEnterprises.presentation.restaurant

import com.foodsaver.app.commonModule.presentation.AppAction
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel

sealed interface RestaurantAction: AppAction {

    data class OnError(val message: String): RestaurantAction
    data class OnZoom(val latitude: Double, val longitude: Double, val zoom: Float = 17.5f): RestaurantAction
    data class OnSetEnterpriseIcon(val enterprises: List<RestaurantModel>): RestaurantAction
    data class OnUpdateUserLocation(val latitude: Double, val longitude: Double): RestaurantAction


}