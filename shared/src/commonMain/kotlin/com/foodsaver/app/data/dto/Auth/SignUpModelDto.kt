package com.foodsaver.app.data.dto.Auth

import kotlinx.serialization.Serializable

@Serializable
internal data class SignUpModelDto(
    val email: String,
    val password: String,
    val displayName: String? = null,
    val returnSecureToken: Boolean = true,
)
