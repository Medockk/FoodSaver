package com.foodsaver.app.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshResponseDto(
    @SerialName("id_token")
    val jwtToken: String,
    @SerialName("expires_in")
    val expiresIn: Long
)
