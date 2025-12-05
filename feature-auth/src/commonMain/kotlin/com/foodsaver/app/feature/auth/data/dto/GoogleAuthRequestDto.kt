package com.foodsaver.app.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthRequestDto(
    val idToken: String
)
