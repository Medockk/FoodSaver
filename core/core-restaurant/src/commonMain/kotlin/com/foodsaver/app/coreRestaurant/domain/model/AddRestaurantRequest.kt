package com.foodsaver.app.coreRestaurant.domain.model

data class AddRestaurantRequest(
    val companyId: String,

    val name: String,
    val description: String,
    val photoUris: List<String>,

    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,

    // address
    val addressName: String,
    val latitude: Double,
    val longitude: Double
)
