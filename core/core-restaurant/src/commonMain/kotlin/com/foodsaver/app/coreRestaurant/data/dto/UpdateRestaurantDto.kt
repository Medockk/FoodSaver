package com.foodsaver.app.coreRestaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateRestaurantDto(
    val restaurantId: String,

    val name: String? = null,
    val description: String? = null,
    val photoUris: List<String>? = null,

    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,

    // address
    val address: Address? = null
) {
    @Serializable
    data class Address(
        val addressName: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null
    )
}
