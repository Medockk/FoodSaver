package com.foodsaver.app.featureAdmin.presentation.viewRrestaurant

import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel

data class ViewRestaurantState(
    val restaurants: List<RestaurantModel> = emptyList(),

)
