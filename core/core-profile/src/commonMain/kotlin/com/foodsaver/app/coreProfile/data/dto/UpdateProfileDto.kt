package com.foodsaver.app.coreProfile.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateProfileDto(
    val fullName: String?,
    val phone: String?,
    val bio: String?,
    val email: String?
)
