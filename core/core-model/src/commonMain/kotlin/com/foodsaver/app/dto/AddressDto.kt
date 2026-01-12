package com.foodsaver.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val id: String,
    val name: String,
    val address: String,
)
