package com.foodsaver.app.coreEnterprises.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class RestaurantDto(
    val id: String,

    val name: String,
    val description: String,
    val photoUris: List<String> = emptyList(),

    val latitude: Double,
    val longitude: Double,
    val addressName: String,

    val rating: Double? = null,
    val deliveryCost: Double? = null,
    val averageDeliveryTime: Double? = null,

    val companyId: String,
)
