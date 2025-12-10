package com.foodsaver.app.feature.auth.domain.model

data class AuthResponseModel(
    val uid: String,
    val username: String,
    val roles: List<String>
)
