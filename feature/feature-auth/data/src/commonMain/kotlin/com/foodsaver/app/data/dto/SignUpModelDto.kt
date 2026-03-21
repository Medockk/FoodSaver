package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class SignUpModelDto(
    val username: String,
    val password: String,
    val displayName: String? = null,
    val email: String? = null,
    val returnSecureToken: Boolean = true,
)
