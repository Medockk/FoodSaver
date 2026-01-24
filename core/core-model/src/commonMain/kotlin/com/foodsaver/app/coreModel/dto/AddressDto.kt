package com.foodsaver.app.coreModel.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val id: String,
    val name: String,
    val address: String,
    val isCurrentAddress: Boolean
)
