package com.foodsaver.app.coreRestaurant.domain.model

data class UpdateRestaurantRequest(
    val restaurantId: String,

    val name: String? = null,
    val description: String? = null,
    val photoUris: List<String>? = null,

    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,

    // address
    val addressName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
