package com.foodsaver.app.domain.model.Auth

data class SignUpModel(
    val email: String,
    val password: String,
    val displayName: String? = null,
)
