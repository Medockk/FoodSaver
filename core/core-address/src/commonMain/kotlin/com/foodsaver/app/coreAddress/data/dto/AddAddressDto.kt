package com.foodsaver.app.coreAddress.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class AddAddressDto(
    val name: String,
    val address: String,
    val isCurrentAddress: Boolean
)
