package com.foodsaver.app.coreEnterprises.domain.model

import com.foodsaver.app.coreModel.model.OrganizationModel

/**
 * @param [deliveryCost] if this is null, to delivery is free!
 */
data class RestaurantModel(
    val id: String,

    val name: String,
    val description: String,
    val photoUris: List<String> = emptyList(),

    val longitude: Double,
    val latitude: Double,
    val addressName: String,

    val rating: Double? = null,
    val deliveryCost: Double? = null,
    val averageDeliveryTime: Double? = null,

    val organization: OrganizationModel
)
