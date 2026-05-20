package com.foodsaver.app.coreRestaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class UserLocationDto(
    val latitude: Double,
    val longitude: Double
)
