package com.foodsaver.app.coreAddress.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class AddAddressDto(
    val latitude: Double,
    val longitude: Double,

    val city: String,
    val street: String,
    val house: String,

    val apartment: String?,
    val floor: Int?,
    val entrance: String?,
)
