package com.foodsaver.app.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshResponseDto(
    val jwtToken: String,
    val expiresIn: Long
)
