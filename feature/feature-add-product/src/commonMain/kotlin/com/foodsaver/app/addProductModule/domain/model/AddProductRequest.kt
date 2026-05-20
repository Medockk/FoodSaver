package com.foodsaver.app.addProductModule.domain.model

import kotlin.time.Instant

data class AddProductRequest(
    val name: String,
    val description: String,

    val imageUris: List<String>,
    val expiresAt: Instant,

    val price: Double,
    val count: Long,

    val unit: String,
    val discount: Double,

    val currency: String,
    val isAvailable: Boolean,

    val ingredientIds: List<String>,
    val categoryIds: List<String>
)
