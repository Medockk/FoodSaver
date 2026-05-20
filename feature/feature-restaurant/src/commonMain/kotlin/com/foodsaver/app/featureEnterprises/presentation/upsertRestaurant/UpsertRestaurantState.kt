package com.foodsaver.app.featureEnterprises.presentation.upsertRestaurant

import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel

data class UpsertRestaurantState(
    val restaurantModel: RestaurantModel? = null,
    val isInfoLoading: Boolean = false,
    val isUpsertLoading: Boolean = false,

    val isGalleryPickerVisible: Boolean = false,

    val companyId: String = "",

    val name: String = "",
    val description: String = "",
    val photoUris: List<String> = emptyList(),

    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,

    val addressName: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)
