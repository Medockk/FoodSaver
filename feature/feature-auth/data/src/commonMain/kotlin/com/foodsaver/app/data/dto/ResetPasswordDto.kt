package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ResetPasswordDto(
    val password: String,
    val confirmPassword: String
)
