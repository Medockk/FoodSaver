package com.foodsaver.app.data.dto.Auth

import kotlinx.serialization.Serializable

@Serializable
internal data class AuthResponseModelDto(
    val uid: String,
    val email: String,
    val roles: List<String>,
    val jwtToken: String,
    val refreshToken: String,
    val expiresIn: Long,
)
