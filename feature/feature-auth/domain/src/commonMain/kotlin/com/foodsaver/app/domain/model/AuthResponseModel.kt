package com.foodsaver.app.domain.model

data class AuthResponseModel(
    val uid: String,
    val permissions: List<String>
)
