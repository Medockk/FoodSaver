package com.foodsaver.app.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class SignInModelDto(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)
