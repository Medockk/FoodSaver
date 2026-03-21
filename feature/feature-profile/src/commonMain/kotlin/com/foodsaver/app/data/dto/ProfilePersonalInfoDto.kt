package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ProfilePersonalInfoDto(
    val fullName: String,
    val email: String,
    val phone: String,
    val bio: String,
    val photoUrl: String?,
)
