package com.foodsaver.app.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseModelDto(
    val uid: String,
    val email: String,
    val roles: List<String>,
    val jwtToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
