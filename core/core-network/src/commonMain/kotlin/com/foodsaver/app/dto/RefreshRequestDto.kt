package com.foodsaver.app.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshRequestDto(
    val refreshToken: String,
    val accessToken: String?
)
