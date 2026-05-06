package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class SignInModelDto(
    val email: String,
    val password: String
)
