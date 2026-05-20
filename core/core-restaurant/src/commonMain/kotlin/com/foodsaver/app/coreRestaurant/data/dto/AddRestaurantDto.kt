package com.foodsaver.app.coreRestaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class AddRestaurantDto(
    val companyId: String,

    val name: String,
    val description: String,
    val photoUris: List<String>,

    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,

    // address
    val address: AddressDto
) {
    @Serializable
    data class AddressDto(
        val addressName: String,
        val latitude: Double,
        val longitude: Double
    )
}
