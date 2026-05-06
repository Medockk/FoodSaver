package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseModelDto(
    val uid: String,
    val permissions: List<String>,
    val accessToken: String,
    val refreshToken: String
)
