package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ForgotPasswordDto(
    val email: String
)
