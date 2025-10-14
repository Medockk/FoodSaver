package com.foodsaver.app.data.dto.Auth

import kotlinx.serialization.Serializable

@Serializable
internal data class SignInModelDto(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true,
)
