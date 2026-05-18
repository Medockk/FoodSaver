package com.foodsaver.app.coreProfile.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileDto(
    val id: String,
    val email: String,
    val fullName: String,
    val imageUri: String?,
    val restaurantId: String?,
    val authorities: List<String>,

    val phone: String?,
    val bio: String?,

    val addressIds: List<String>,
    val currentAddressId: String?,
    val currentPaymentMethodId: String?
)
