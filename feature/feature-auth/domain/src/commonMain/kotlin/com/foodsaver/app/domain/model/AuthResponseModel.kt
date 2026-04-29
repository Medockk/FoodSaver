package com.foodsaver.app.domain.model

data class AuthResponseModel(
    val uid: String,
    val username: String,
    val roles: List<String>
)
