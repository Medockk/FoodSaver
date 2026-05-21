package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthRequestDto(
    val googleId: String
)
