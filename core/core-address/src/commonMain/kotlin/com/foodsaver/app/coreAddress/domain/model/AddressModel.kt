package com.foodsaver.app.coreAddress.domain.model

data class AddressModel(
    val id: String,
    val name: String,

    val latitude: Double,
    val longitude: Double,

    val city: String,
    val street: String,
    val house: String,

    val apartment: String?,
    val floor: Int?,
    val entrance: String?,

    val fullAddress: String
)
