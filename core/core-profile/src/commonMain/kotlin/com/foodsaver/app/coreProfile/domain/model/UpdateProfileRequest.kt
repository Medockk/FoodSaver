package com.foodsaver.app.coreProfile.domain.model

data class UpdateProfileRequest(
    val fullName: String?,
    val phone: String?,
    val bio: String?,
    val email: String?
)
