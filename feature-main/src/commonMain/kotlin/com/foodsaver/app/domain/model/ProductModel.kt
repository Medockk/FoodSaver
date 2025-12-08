package com.foodsaver.app.domain.model

data class ProductModel(
    val productId: String,
    val name: String,
    val description: String,

    val cost: Float,
    val rating: Float?,
    val organization: String,
    val count: Int,
    val categoryIds: List<String>,
    val photoUrl: String?,

    val expiresAt: String
)
