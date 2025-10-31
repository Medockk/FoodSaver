package com.foodsaver.app.feature.auth.domain.model

data class AuthResponseModel(
    val uid: String,
    val email: String,
    val roles: List<String>
)
