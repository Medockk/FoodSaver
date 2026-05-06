package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class SignUpModelDto(
    val fullName: String,
    val email: String,
    val password: String
)
