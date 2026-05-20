package com.foodsaver.app.featureRestaurant.viewRestaurant

import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel

data class ViewRestaurantState(
    val restaurants: List<RestaurantModel> = emptyList(),

)
